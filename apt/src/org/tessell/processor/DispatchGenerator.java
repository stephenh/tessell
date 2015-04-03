package org.tessell.processor;

import static joist.sourcegen.Argument.arg;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import joist.sourcegen.Argument;
import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Copy;
import joist.util.Function1;
import joist.util.Join;

import org.exigencecorp.aptutil.GenericSuffix;
import org.exigencecorp.aptutil.Prop;
import org.exigencecorp.aptutil.PropUtil;
import org.exigencecorp.aptutil.Util;
import org.tessell.GenDispatch;
import org.tessell.In;
import org.tessell.Out;

public class DispatchGenerator {

  private final ProcessingEnvironment env;
  private final TypeElement element;
  private final GClass actionClass;
  private final GClass resultClass;
  private final GenericSuffix generics;
  private final Map<Integer, VariableElement> inParams = new TreeMap<Integer, VariableElement>();
  private final Map<Integer, VariableElement> outParams = new TreeMap<Integer, VariableElement>();
  private final String simpleName;
  private final String dispatchPackageName;

  public DispatchGenerator(ProcessingEnvironment env, TypeElement element) throws InvalidTypeElementException {
    if (!element.toString().endsWith("Spec")) {
      env.getMessager().printMessage(Kind.ERROR, "GenDispatch targets must end with a Spec suffix", element);
      throw new InvalidTypeElementException();
    }

    this.env = env;
    this.element = element;
    this.generics = new GenericSuffix(element);
    simpleName = element.toString().replaceAll("Spec$", "");
    dispatchPackageName = detectDispatchBasePackage(env);

    this.actionClass = new GClass(simpleName + "Action" + generics.varsWithBounds);
    this.resultClass = new GClass(simpleName + "Result" + generics.varsWithBounds);
  }

  public void generate() {
    setResultBaseClassOrInterface();
    setActionBaseClassOrInterface();
    addAnnotatedInAndOutParams();
    generateDto(actionClass, MpvUtil.toProperties(inParams.values()));
    generateDto(resultClass, MpvUtil.toProperties(outParams.values()));
    makeUiCommandIfOnClasspath();
  }

  private void makeUiCommandIfOnClasspath() {
    if (env.getElementUtils().getTypeElement("org.tessell.model.commands.DispatchUiCommand") == null) {
      return;
    }
    if (!dispatchPackageName.contains("org.tessell")) {
      return;
    }
    GClass command = new GClass(simpleName + "Command" + generics.varsWithBounds);
    command.setAbstract().baseClassName(
      "org.tessell.model.commands.DispatchUiCommand<{}Action{}, {}Result{}>",
      simpleName,
      generics.vars,
      simpleName,
      generics.vars);

    command.getConstructor(arg("org.tessell.dispatch.client.util.OutstandingDispatchAsync", "async")).body.line("super(async);");

    String actionWithoutBounds = simpleName + "Action" + generics.vars;

    // if no arguments to the action, just implement createAction for the user
    if (inParams.size() == 0) {
      GMethod createAction = command.getMethod("createAction").setProtected().returnType(actionWithoutBounds);
      createAction.addAnnotation("@Override");
      createAction.body.line("return new {}();", actionWithoutBounds);
    } else {
      // add createAction helper method with all the incoming params (to avoid a long new Xxx(...) call)
      GMethod createActionHelper = command.getMethod("createAction", asArguments(inParams.values())).setProtected().returnType(actionWithoutBounds);
      createActionHelper.body.line("return new {}({});", actionWithoutBounds, Join.commaSpace(asNames(inParams.values())));
      // add an execute overload that will make an action
      GMethod executeOverload = command.getMethod("execute", asArguments(inParams.values()));
      executeOverload.body.line("doExecute(createAction({}));", Join.commaSpace(asNames(inParams.values())));
    }

    PropUtil.addGenerated(command, DispatchGenerator.class);
    Util.saveCode(env, command, element);
  }

  private void addAnnotatedInAndOutParams() {
    for (VariableElement field : ElementFilter.fieldsIn(element.getEnclosedElements())) {
      In in = field.getAnnotation(In.class);
      Out out = field.getAnnotation(Out.class);
      if (in != null) {
        addInParam(field, in);
      } else if (out != null) {
        addOutParam(field, out);
      } else {
        env.getMessager().printMessage(Kind.ERROR, field.getSimpleName().toString() + " must be annotated with @In or @Out", field);
      }
    }
  }

  private void addInParam(VariableElement field, In in) {
    if (inParams.containsKey(in.value())) {
      env.getMessager().printMessage(Kind.ERROR, field.getSimpleName().toString() + " reuses an order value", field);
    } else {
      inParams.put(in.value(), field);
    }
  }

  private void addOutParam(VariableElement field, Out out) {
    if (outParams.containsKey(out.value())) {
      env.getMessager().printMessage(Kind.ERROR, field.getSimpleName().toString() + " reuses an order value", field);
    } else {
      outParams.put(out.value(), field);
    }
  }

  private void setActionBaseClassOrInterface() {
    GenDispatch genDispatch = element.getAnnotation(GenDispatch.class);
    if (genDispatch.baseAction() != null && genDispatch.baseAction().length() > 0) {
      this.actionClass.baseClassName("{}<{}>", genDispatch.baseAction(), simpleName + "Result" + generics.vars);
    } else {
      this.actionClass.implementsInterface("{}.Action<{}>", dispatchPackageName, simpleName + "Result" + generics.vars);
    }
  }

  private void setResultBaseClassOrInterface() {
    GenDispatch genDispatch = element.getAnnotation(GenDispatch.class);
    if (genDispatch.baseResult() != null && genDispatch.baseResult().length() > 0) {
      this.resultClass.baseClassName(genDispatch.baseResult());
    } else {
      this.resultClass.implementsInterface("{}.Result", dispatchPackageName);
    }
  }

  private void generateDto(GClass gclass, List<Prop> properties) {
    PropUtil.addGenerated(gclass, DispatchGenerator.class);
    // move to GClass as a utility method
    GMethod cstr = gclass.getConstructor();
    for (Prop p : properties) {
      addFieldAndGetterAndConstructorArg(gclass, cstr, p.name, p.type);
    }
    if (properties.size() > 0) {
      // re-add the default constructor for serialization
      gclass.getConstructor().setProtected();
    }
    PropUtil.addHashCode(gclass, properties);
    PropUtil.addEquals(gclass, generics, properties);
    PropUtil.addToString(gclass, properties);
    Util.saveCode(env, gclass);
  }

  private String detectDispatchBasePackage(ProcessingEnvironment env) {
    String dispatchBasePackage = env.getOptions().get("dispatchBasePackage");
    if (dispatchBasePackage != null) {
      return dispatchBasePackage;
    }
    for (String option : new String[] {
      "org.tessell.dispatch.shared.Action",
      "net.customware.gwt.dispatch.shared.Action",
      "com.gwtplatform.dispatch.shared.Action" }) {
      TypeElement t = env.getElementUtils().getTypeElement(option);
      if (t != null) {
        return ((PackageElement) t.getEnclosingElement()).getQualifiedName().toString();
      }
    }
    return "org.tessell.dispatch.shared";
  }

  private void addFieldAndGetterAndConstructorArg(GClass gclass, GMethod cstr, String name, String type) {
    gclass.getField(name).type(type);
    gclass.getMethod("get" + Util.upper(name)).returnType(type).body.append("return this.{};", name);
    cstr.argument(type, name);
    cstr.body.line("this.{} = {};", name, name);
  }

  private static List<Argument> asArguments(Collection<VariableElement> elements) {
    return Copy.list(elements).map(new Function1<Argument, VariableElement>() {
      public Argument apply(VariableElement p1) {
        return new Argument(p1.asType().toString(), p1.getSimpleName().toString());
      }
    });
  }

  private static List<String> asNames(Collection<VariableElement> elements) {
    return Copy.list(elements).map(new Function1<String, VariableElement>() {
      public String apply(VariableElement p1) {
        return p1.getSimpleName().toString();
      }
    });
  }

}
