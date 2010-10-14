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
  final String baseName;
  final GClass gwtView;
  final GClass isView;
  final GClass stubView;

  // the handler is only created if we have to parser the ui.xml file
  private UiXmlHandler handler;

  UiXmlFile(ViewGenerator viewGenerator, final File uiXml) {
    this.viewGenerator = viewGenerator;
    this.uiXml = uiXml;

    final String templateClassName = deriveClassName();
    final String packageName = StringUtils.substringBeforeLast(templateClassName, ".");
    baseName = StringUtils.substringAfterLast(templateClassName, ".");
    isView = new GClass(packageName + ".Is" + baseName);
    gwtView = new GClass(packageName + ".Gwt" + baseName);
    stubView = new GClass(packageName + ".Stub" + baseName);
  }

  /** @return whether the {@code ui.xml} file is newer than the last {@code IsXxx} output. */
  public boolean hasChanged() {
    File isFile = new File(viewGenerator.output, isView.getFileName());
    if (!isFile.exists()) {
      return true;
    }
    return uiXml.lastModified() > isFile.lastModified();
  }

  public void generate() throws Exception {
    System.out.println(uiXml);
    handler = new UiXmlHandler();
    viewGenerator.parser.parse(uiXml, handler);

    generateIsView();
    generateGwtView();
    generateStubView();
  }

  private void generateIsView() throws Exception {
    isView.baseClass(IsWidget.class);
    isView.setInterface();
    isView.getMethod("asWidget").returnType(Widget.class);
    isView.getMethod("setDebugId").argument("String", "baseDebugId");

    // methods for each ui:field
    for (final UiFieldDeclaration uiField : handler.uiFields) {
      isView.getMethod(uiField.name).returnType(viewGenerator.config.getInterface(uiField.type));
    }
    // methods for each ui:style
    for (final UiStyleDeclaration style : handler.styleFields) {
      isView.getMethod(style.name).returnType(style.type);
    }

    viewGenerator.markAndSave(isView);
  }

  private void generateGwtView() throws Exception {
    gwtView.baseClass(DelegateIsWidget.class).implementsInterface(isView.getSimpleClassName());
    final GMethod cstr = gwtView.getConstructor();
    gwtView.addImports(GWT.class);
    if (handler.withFields.size() > 0 || handler.uiFields.size() > 0) {
      gwtView.addImports(UiField.class);
    }

    final GMethod debugId = gwtView.getMethod("setDebugId").argument("String", "baseDebugId");

    {
      // uibinder
      final GClass uibinder = gwtView.getInnerClass("MyUiBinder").setInterface();
      uibinder.baseClassName("{}<{}, {}>", UiBinder.class.getName(), handler.firstTagType, gwtView.getSimpleClassName());
      uibinder.addAnnotation("@UiTemplate(\"{}\")", uiXml.getName());
      gwtView.addImports(UiTemplate.class);

      gwtView.getField("binder").type("MyUiBinder").setStatic().setFinal().initialValue("GWT.create(MyUiBinder.class)");
    }

    // for each ui:with, add a @UiField(provided=true) field and a constructor arg
    for (final UiFieldDeclaration field : handler.withFields) {
      gwtView.getField(field.name).type(field.type).setAccess(Access.PACKAGE).addAnnotation("@UiField(provided = true)");
      cstr.argument(field.type, field.name);
      cstr.body.line("this.{} = {};", field.name, field.name);
    }

    // for each ui:style, make @UiField fields, getter methods, plus Css class for each ui:style
    for (final UiStyleDeclaration style : handler.styleFields) {
      gwtView.getField(style.name).type(style.type).setAccess(Access.PACKAGE).addAnnotation("@UiField");
      gwtView.getMethod(style.name).returnType(style.type).body.line("return {};", style.name);
      new CssGenerator(style.getCssInFile(), viewGenerator.cleanup, style.type, viewGenerator.output).run();
    }

    // for each ui:field, make @UiField (usually provided=true) and getter methods
    for (final UiFieldDeclaration field : handler.uiFields) {
      final String interfaceType = viewGenerator.config.getInterface(field.type);
      final String subType = viewGenerator.config.getSubclass(field.type);
      final GField f = gwtView.getField(field.name);
      final GMethod m = gwtView.getMethod(field.name).returnType(interfaceType);

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
        gwtView.addImports(UIObject.class);
      } else {
        debugId.body.line("{}.ensureDebugId(baseDebugId + \"-{}\");", field.name, field.name);
      }
    }

    cstr.body.line("setWidget(binder.createAndBindUi(this));");
    cstr.body.line("setDebugId(\"{}\");", gwtView.getSimpleClassNameWithoutGeneric().replaceAll("View$", "").replaceAll("^Gwt", ""));

    viewGenerator.markAndSave(gwtView);
  }

  private void generateStubView() throws Exception {
    stubView.baseClass(StubWidget.class).implementsInterface(isView.getSimpleClassName());

    final GMethod debugId = stubView.getMethod("setDebugId").argument("String", "baseDebugId");

    // for each ui:field, add a field assigned to the stub type, and a getter method
    for (final UiFieldDeclaration field : handler.uiFields) {
      final String stubType = viewGenerator.config.getStub(field.type);
      stubView.getField(field.name).type(stubType).setPublic().setFinal().initialValue("new {}()", stubType);
      stubView.getMethod(field.name).returnType(stubType).body.line("return {};", field.name);
      debugId.body.line("{}.ensureDebugId(baseDebugId + \"-{}\");", field.name, field.name);
    }

    // for each ui:style, make @UiField fields, getter method, plus StubCss class
    for (final UiStyleDeclaration style : handler.styleFields) {
      CssStubGenerator g = new CssStubGenerator(style.getCssInFile(), viewGenerator.cleanup, style.type, viewGenerator.output);
      g.run();
      stubView.getField(style.name).type(style.type).setFinal().initialValue("new {}()", g.getCssStubClassName());
      stubView.getMethod(style.name).returnType(style.type).body.line("return {};", style.name);
    }

    stubView.getConstructor().body.line(
      "setDebugId(\"{}\");",
      stubView.getSimpleClassNameWithoutGeneric().replaceAll("View$", "").replaceAll("^Stub", ""));

    viewGenerator.markAndSave(stubView);
  }

  String getPath() {
    return uiXml.getPath();
  }

  /** @return the ui:with fields, if we parsed the file. */
  List<UiFieldDeclaration> getWiths() {
    return handler.withFields;
  }

  List<UiFieldDeclaration> getWithsPossiblyCached() {
    return handler == null ? viewGenerator.cache.getCachedWiths(this) : getWiths();
  }

  /** @return the ui:style fields, if we parsed the file. */
  List<UiStyleDeclaration> getStyles() {
    return handler.styleFields;
  }

  List<UiStyleDeclaration> getStylesPossiblyCached() {
    return handler == null ? viewGenerator.cache.getCachedStyles(this) : getStyles();
  }

  private String deriveClassName() {
    // Get the absolute path, drop off the input path, drop ui.xml, / -> .
    return appendViewIfNeeded(uiXml
      .getAbsolutePath()
      .replace(viewGenerator.input.getPath() + File.separator, "")
      .replace(".ui.xml", "")
      .replace("/", "."));
  }

  private String appendViewIfNeeded(String input) {
    return input.endsWith("View") ? input : input + "View";
  }

}