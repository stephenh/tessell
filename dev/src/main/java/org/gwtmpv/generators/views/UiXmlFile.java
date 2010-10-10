package org.gwtmpv.generators.views;

import java.io.File;
import java.util.List;

import joist.sourcegen.Access;
import joist.sourcegen.GClass;
import joist.sourcegen.GField;
import joist.sourcegen.GMethod;

import org.apache.commons.lang.StringUtils;
import org.gwtmpv.generators.css.CssGenerator;
import org.gwtmpv.generators.css.CssStubGenerator;
import org.gwtmpv.widgets.DelegateIsWidget;
import org.gwtmpv.widgets.GwtElement;
import org.gwtmpv.widgets.IsWidget;
import org.gwtmpv.widgets.StubWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/** A {@code ui.xml} file. */
class UiXmlFile {

  private final ViewGenerator viewGenerator;
  private final File uiXml;
  private final String fileName;
  final String simpleName;
  final String gwtName;
  final String interfaceName;
  final String stubName;
  // the handler is only created if we have to parser the ui.xml file
  private UiXmlHandler handler;

  UiXmlFile(ViewGenerator viewGenerator, final File uiXml) {
    this.viewGenerator = viewGenerator;
    this.uiXml = uiXml;
    String className = uiXml
      .getAbsolutePath()
      .replace(this.viewGenerator.input.getPath() + File.separator, "")
      .replace(".ui.xml", "")
      .replace("/", ".");
    fileName = StringUtils.substringAfterLast(className, ".");
    simpleName = fileName.endsWith("View") ? fileName : fileName + "View";
    final String packageName = StringUtils.substringBeforeLast(className, ".");
    gwtName = packageName + ".Gwt" + simpleName;
    interfaceName = packageName + ".Is" + simpleName;
    stubName = packageName + ".Stub" + simpleName;
  }

  boolean hasChanged() {
    File interfaceFile = new File(viewGenerator.output.getPath() + File.separator + interfaceName.replace(".", File.separator) + ".java");
    if (!interfaceFile.exists()) {
      return true;
    }
    return uiXml.lastModified() > interfaceFile.lastModified();
  }

  void generate() throws Exception {
    System.out.println(uiXml);
    handler = new UiXmlHandler();
    viewGenerator.parser.parse(uiXml, handler);

    generateInterface();
    generateView();
    generateStub();
  }

  private void generateInterface() throws Exception {
    final GClass i = new GClass(interfaceName);
    i.baseClass(IsWidget.class);
    i.setInterface();
    i.getMethod("asWidget").returnType(Widget.class);
    i.getMethod("setDebugId").argument("String", "baseDebugId");

    for (final UiFieldDeclaration uiField : handler.uiFields) {
      i.getMethod(uiField.name).returnType(viewGenerator.config.getInterface(uiField.type));
    }

    for (final UiStyleDeclaration style : handler.styleFields) {
      i.getMethod(style.name).returnType(style.type);
    }

    viewGenerator.save(i);
  }

  private void generateView() throws Exception {
    final GClass v = new GClass(gwtName).baseClass(DelegateIsWidget.class).implementsInterface(interfaceName);
    final GMethod cstr = v.getConstructor();
    v.addImports(GWT.class);
    if (handler.withFields.size() > 0 || handler.uiFields.size() > 0) {
      v.addImports(UiField.class);
    }

    final GMethod debugId = v.getMethod("setDebugId").argument("String", "baseDebugId");

    final GClass uibinder = v.getInnerClass("MyUiBinder").setInterface();
    uibinder.baseClassName("{}<{}, {}>", UiBinder.class.getName(), handler.firstTagType, gwtName);
    uibinder.addAnnotation("@UiTemplate(\"{}.ui.xml\")", fileName);
    v.addImports(UiTemplate.class);

    v.getField("binder").type("MyUiBinder").setStatic().setFinal().initialValue("GWT.create(MyUiBinder.class)");

    for (final UiFieldDeclaration field : handler.withFields) {
      v.getField(field.name).type(field.type).setAccess(Access.PACKAGE).addAnnotation("@UiField(provided = true)");
      cstr.argument(field.type, field.name);
      cstr.body.line("this.{} = {};", field.name, field.name);
    }

    // Make fields, getter, plus Css class for each ui:style
    for (final UiStyleDeclaration style : handler.styleFields) {
      v.getField(style.name).type(style.type).setAccess(Access.PACKAGE).initialValue("GWT.create({}.class)", style.type);
      v.getMethod(style.name).returnType(style.type).body.line("return {};", style.name);
      new CssGenerator(style.getCssInFile(), style.type, viewGenerator.output).run();
    }

    for (final UiFieldDeclaration field : handler.uiFields) {
      final String interfaceType = viewGenerator.config.getInterface(field.type);
      final String subType = viewGenerator.config.getSubclass(field.type);
      final GField f = v.getField(field.name);
      final GMethod m = v.getMethod(field.name).returnType(interfaceType);

      if (field.isElement) {
        f.type(field.type).setAccess(Access.PACKAGE).addAnnotation("@UiField");
        m.body.line("return new {}({});", GwtElement.class.getName(), field.name);
      } else if (field.type.endsWith("HTMLPanel") || field.type.endsWith("RadioButton")) {
        f.type(field.type).setAccess(Access.PACKAGE).addAnnotation("@UiField");
        m.body.line("return new {}({});", subType, field.name);
      } else {
        f.type(subType).setFinal().setAccess(Access.PACKAGE).addAnnotation("@UiField(provided = true)").initialValue("new {}()", subType);
        m.body.line("return {};", field.name);
      }

      if (field.isElement) {
        debugId.body.line("UIObject.ensureDebugId({}, baseDebugId + \"-{}\");", field.name, field.name);
        v.addImports(UIObject.class);
      } else {
        debugId.body.line("{}.ensureDebugId(baseDebugId + \"-{}\");", field.name, field.name);
      }
    }

    cstr.body.line("setWidget(binder.createAndBindUi(this));");
    cstr.body.line("setDebugId(\"{}\");", v.getSimpleClassNameWithoutGeneric().replaceAll("View$", "").replaceAll("^Gwt", ""));

    viewGenerator.save(v);
  }

  private void generateStub() throws Exception {
    final GClass s = new GClass(stubName).baseClass(StubWidget.class).implementsInterface(interfaceName);

    final GMethod debugId = s.getMethod("setDebugId").argument("String", "baseDebugId");

    for (final UiFieldDeclaration field : handler.uiFields) {
      final String stubType = viewGenerator.config.getStub(field.type);
      if (stubType == null) {
        throw new RuntimeException("No stub for " + field.type);
      }
      s.getField(field.name).type(stubType).setPublic().setFinal().initialValue("new {}()", stubType);
      s.getMethod(field.name).returnType(stubType).body.line("return {};", field.name);
      debugId.body.line("{}.ensureDebugId(baseDebugId + \"-{}\");", field.name, field.name);
    }

    // Make fields, getter, plus StubCss class for each ui:style
    for (final UiStyleDeclaration style : handler.styleFields) {
      CssStubGenerator g = new CssStubGenerator(style.getCssInFile(), style.type, viewGenerator.output);
      g.run();
      s.getField(style.name).type(style.type).setFinal().initialValue("new {}()", g.getCssStubClassName());
      s.getMethod(style.name).returnType(style.type).body.line("return {};", style.name);
    }

    s.getConstructor().body.line("setDebugId(\"{}\");", s.getSimpleClassNameWithoutGeneric().replaceAll("View$", "").replaceAll("^Stub", ""));

    viewGenerator.save(s);
  }

  String getPath() {
    return uiXml.getPath();
  }

  List<UiFieldDeclaration> getWithTypes() {
    if (handler == null) {
      return viewGenerator.cache.getWithTypes(this);
    } else {
      return handler.withFields;
    }
  }
}
