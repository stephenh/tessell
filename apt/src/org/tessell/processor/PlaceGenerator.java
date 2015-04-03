package org.tessell.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import joist.sourcegen.Argument;
import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Join;

import org.exigencecorp.aptutil.Util;
import org.tessell.GenPlace;

/**
 * Generates Place classes that wrap the boilerplate {@code GWT.runAsync} and call presenter static methods when requested.
 *
 * For example:
 * 
 * <pre>
 *    public class FooPresenter {
 *      &#064;GenPlace("foo")
 *      public static void handleRequest(AppWideState state) {
 *        // called when FooPlace is fired
 *        state.doStuffToShowMe();
 *      }
 *    }
 * </pre>
 *
 * Generates a {@code FooPlace} class that takes an {@code AppWideState} state
 * as its constructor, e.g.:
 * 
 * <pre>
 *     PlaceManager m = ...;
 *     m.registerPlace(new FooPlace(appWideState));
 * </pre>
 */
public class PlaceGenerator {

  private final ProcessingEnvironment env;
  private final ExecutableElement element;
  private final GenPlace place;
  private final GClass placeClass;
  private final GClass placeRequestClass;

  public PlaceGenerator(ProcessingEnvironment env, ExecutableElement element, GenPlace place) throws InvalidTypeElementException {
    if (!element.getModifiers().contains(Modifier.STATIC)) {
      env.getMessager().printMessage(Kind.ERROR, "GenPlace methods must be static", element);
      throw new InvalidTypeElementException();
    }
    this.env = env;
    this.element = element;
    this.place = place;
    placeClass = new GClass(getPlaceQualifiedClassName()).baseClassName("org.tessell.place.Place");
    placeRequestClass = new GClass(getPlaceRequestQualifiedClassName()).baseClassName("org.tessell.place.PlaceRequest");
  }

  public void generate() {
    // PlaceClass
    GMethod cstr = placeClass.getConstructor();
    addStaticPlaceName();
    addStaticNewRequest();
    addCstrSuperCall(cstr);
    addCstrStaticMethodArguments(cstr);
    addCstrFailureCallbackIfNeeded(cstr);
    if (place.async()) {
      addAsyncHandleRequest();
    } else {
      addSyncHandleRequest();
    }
    Util.saveCode(env, placeClass, element);
    // PlaceRequestClass
    addPlaceRequestCstrOne();
    addPlaceRequestCstrTwo();
    addPlaceRequestCstrThree();
    addPlaceRequestParameters();
    Util.saveCode(env, placeRequestClass, element);
  }

  private void addStaticPlaceName() {
    placeClass.getField("NAME").type("String").setPublic().setStatic().setFinal().initialValue("\"{}\"", place.name());
  }

  private void addStaticNewRequest() {
    GMethod m = placeClass.getMethod("newRequest").setStatic().returnType(placeRequestClass.getSimpleName());
    m.body.line("return new {}();", placeRequestClass.getSimpleName());
  }

  private void addCstrSuperCall(GMethod cstr) {
    cstr.body.line("super(NAME);");
  }

  private void addCstrStaticMethodArguments(GMethod cstr) {
    // any of the static method arguments become constructor arguments
    for (VariableElement param : element.getParameters()) {
      String paramName = param.getSimpleName().toString();
      String paramType = param.asType().toString();

      // this isn't a static argument
      if (paramType.equals("org.tessell.place.PlaceRequest")) {
        continue;
      }

      placeClass.getField(paramName).type(paramType).setFinal();
      cstr.argument(paramType, paramName);
      cstr.body.line("this.{} = {};", paramName, paramName);
    }
  }

  private void addCstrFailureCallbackIfNeeded(GMethod cstr) {
    if (place.async()) {
      placeClass.getField("failureCallback").type("org.tessell.util.FailureCallback").setFinal();
      cstr.argument("org.tessell.util.FailureCallback", "failureCallback");
      cstr.body.line("this.{} = {};", "failureCallback", "failureCallback");
    }
  }

  private void addAsyncHandleRequest() {
    GMethod m = placeClass.getMethod("handleRequest").argument("final org.tessell.place.PlaceRequest", "request");
    m.body.line("GWT.runAsync(new RunAsyncCallback() {");
    m.body.line("    public void onSuccess() {");
    m.body.line("        if (request == null) {");
    m.body.line("            return; // prefetching");
    m.body.line("        }");
    m.body.line("        {}.{}({});", getPresenterClassName(), getMethodName(), Join.commaSpace(getMethodParamNames()));
    m.body.line("    }");
    m.body.line("");
    m.body.line("    public void onFailure(Throwable caught) {");
    m.body.line("        failureCallback.onFailure(caught);");
    m.body.line("    }");
    m.body.line("});");
    placeClass.addImports("com.google.gwt.core.client.GWT", "com.google.gwt.core.client.RunAsyncCallback");
  }

  private void addSyncHandleRequest() {
    GMethod m = placeClass.getMethod("handleRequest").argument("final org.tessell.place.PlaceRequest", "request");
    m.body.line("if (request == null) {");
    m.body.line("    return; // prefetching (not needed, just for consistency)");
    m.body.line("}");
    m.body.line("{}.{}({});", getPresenterClassName(), getMethodName(), Join.commaSpace(getMethodParamNames()));
  }

  private List<String> getMethodParamNames() {
    List<String> paramNames = new ArrayList<String>();
    for (VariableElement param : element.getParameters()) {
      paramNames.add(param.getSimpleName().toString());
    }
    return paramNames;
  }

  private String getMethodName() {
    return element.getSimpleName().toString();
  }

  private String getPresenterClassName() {
    return ((TypeElement) element.getEnclosingElement()).getSimpleName().toString();
  }

  private String getPlaceQualifiedClassName() {
    return ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString().replace("Presenter", "Place");
  }

  private String getPlaceRequestQualifiedClassName() {
    return ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString().replace("Presenter", "PlaceRequest");
  }

  private void addPlaceRequestCstrOne() {
    GMethod cstr = placeRequestClass.getConstructor(Argument.arg("PlaceRequest", "request"));
    cstr.body.line("super(request);");
    cstr.body.line("if (!{}.NAME.equals(request.getName())) {", placeClass.getSimpleName());
    cstr.body.line("    throw new IllegalArgumentException(\"Wrong place for class: \" + request.getName());");
    cstr.body.line("}");
  }

  private void addPlaceRequestCstrTwo() {
    GMethod cstr = placeRequestClass.getConstructor();
    cstr.body.line("super({}.NAME);", placeClass.getSimpleName());
  }

  private void addPlaceRequestCstrThree() {
    if (place.params() == null || place.params().length == 0) {
      return;
    }
    GMethod cstr = placeRequestClass.getConstructor(//
      Argument.arg(placeRequestClass.getSimpleName(), "request"),
      Argument.arg("String", "param"),
      Argument.arg("String", "value")).setPrivate();
    cstr.body.line("super(request, param, value);");
  }

  private void addPlaceRequestParameters() {
    if (place.params() == null) {
      return;
    }
    for (String param : place.params()) {
      GMethod getter = placeRequestClass.getMethod(param).returnType("String");
      getter.body.line("return getParameter(\"{}\", null);", param);

      GMethod getterWithDefault = placeRequestClass.getMethod(param + "Or", Argument.arg("String", "def")).returnType("String");
      getterWithDefault.body.line("return getParameter(\"{}\", def);", param);

      GMethod setter = placeRequestClass.getMethod(param, Argument.arg("Object", "value")).returnType(placeRequestClass.getSimpleName());
      setter.body.line("return new {}(this, \"{}\", ObjectUtils.toStr(value, \"\"));", placeRequestClass.getSimpleName(), param);
      placeRequestClass.addImports("org.tessell.util.ObjectUtils");
    }
  }

}
