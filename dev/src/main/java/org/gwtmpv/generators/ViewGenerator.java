package org.gwtmpv.generators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import joist.sourcegen.Access;
import joist.sourcegen.GClass;
import joist.sourcegen.GField;
import joist.sourcegen.GMethod;
import joist.util.Join;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.gwtmpv.widgets.DelegateIsWidget;
import org.gwtmpv.widgets.GwtElement;
import org.gwtmpv.widgets.IsWidget;
import org.gwtmpv.widgets.StubWidget;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ViewGenerator {

  private final File input;
  private final File output;
  private final String packageName;
  private final List<UiXmlFile> uiXmlFiles = new ArrayList<UiXmlFile>();
  private final ViewGeneratorConfig config = new ViewGeneratorConfig();
  private final ViewGeneratorCache cache = new ViewGeneratorCache();
  private final SAXParser parser;

  public ViewGenerator(final File inputDirectory, final String packageName, final File outputDirectory) {
    input = inputDirectory.getAbsoluteFile();
    output = outputDirectory.getAbsoluteFile();
    this.packageName = packageName;
    parser = makeNewParser();
  }

  public static void main(final String[] args) throws Exception {
    final Map<String, String> settings = GenUtils.parseArgs(args);
    final File inputDirectory = new File(settings.get("inputDirectory"));
    final File outputDirectory = new File(settings.get("outputDirectory"));
    final String packageName = settings.get("packageName");
    final ViewGenerator g = new ViewGenerator(inputDirectory, packageName, outputDirectory);
    g.generate();
  }

  private void generate() throws Exception {
    long start = System.currentTimeMillis();

    cache.loadIfExists();
    for (final File uiXml : findUiXmlFiles()) {
      if (uiXml.getName().contains("-nogen.")) {
        continue;
      }
      uiXmlFiles.add(new UiXmlFile(uiXml));
    }

    for (final UiXmlFile uiXml : uiXmlFiles) {
      if (uiXml.hasChanged() || !cache.has(uiXml)) {
        uiXml.generate();
        cache.update(uiXml);
      }
    }

    generateAppViews();
    generateGwtViews();
    generateStubViews();

    cache.save();

    long end = System.currentTimeMillis();
    System.out.println("Done " + (end - start) + "ms");
  }

  private void generateAppViews() {
    final GClass appViews = new GClass(packageName + ".AppViews").setInterface();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      appViews.getMethod("get" + uiXml.simpleName).returnType(uiXml.interfaceName);
    }
    save(appViews);
  }

  private void generateGwtViews() {
    final GClass gwtViews = new GClass(packageName + ".GwtViews").implementsInterface("AppViews");
    final GMethod cstr = gwtViews.getConstructor();
    for (final UiFieldDeclaration with : allWiths()) {
      gwtViews.getField(with.name).type(with.type).setFinal();
      cstr.argument(with.type, with.name);
      cstr.body.line("this.{} = {};", with.name, with.name);
    }
    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = gwtViews.getMethod("get" + uiXml.simpleName).returnType(uiXml.interfaceName);
      final List<String> withFieldNames = new ArrayList<String>();
      for (final UiFieldDeclaration with : uiXml.getWithTypes()) {
        withFieldNames.add("this." + with.name);
      }
      m.addAnnotation("@Override");
      m.body.line("return new {}({});", uiXml.gwtName, Join.commaSpace(withFieldNames));
    }
    save(gwtViews);
  }

  private void generateStubViews() {
    final GClass stubViews = new GClass(packageName + ".StubViews").implementsInterface("AppViews");
    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = stubViews.getMethod("get" + uiXml.simpleName).returnType(uiXml.stubName);
      m.addAnnotation("@Override");
      m.body.line("return new {}();", uiXml.stubName);
    }
    save(stubViews);
  }

  private Collection<UiFieldDeclaration> allWiths() {
    final Map<String, UiFieldDeclaration> map = new HashMap<String, UiFieldDeclaration>();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      for (final UiFieldDeclaration field : uiXml.getWithTypes()) {
        map.put(field.name, field);
      }
    }
    return new TreeSet<UiFieldDeclaration>(map.values());
  }

  private void save(final GClass gclass) {
    try {
      FileUtils.writeStringToFile(new File(output, gclass.getFileName()), gclass.toCode());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private Collection<File> findUiXmlFiles() {
    return FileUtils.listFiles(input, new String[] { "ui.xml" }, true);
  }

  private SAXParser makeNewParser() {
    final SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(true);
    try {
      return factory.newSAXParser();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
  }

  // package private so ViewGeneratorCache can use it
  class UiXmlFile {
    private final File uiXml;
    private final String fileName;
    private final String simpleName;
    private final String gwtName;
    private final String interfaceName;
    private final String stubName;
    // the handler is only created if we have to parser the ui.xml file
    private UiXmlHandler handler;

    private UiXmlFile(final File uiXml) {
      this.uiXml = uiXml;
      String className = uiXml.getAbsolutePath().replace(input.getPath() + File.separator, "").replace(".ui.xml", "").replace("/", ".");
      fileName = StringUtils.substringAfterLast(className, ".");
      simpleName = fileName.endsWith("View") ? fileName : fileName + "View";
      final String packageName = StringUtils.substringBeforeLast(className, ".");
      gwtName = packageName + ".Gwt" + simpleName;
      interfaceName = packageName + ".Is" + simpleName;
      stubName = packageName + ".Stub" + simpleName;
    }

    private boolean hasChanged() {
      File interfaceFile = new File(output.getPath() + File.separator + interfaceName.replace(".", File.separator) + ".java");
      if (!interfaceFile.exists()) {
        return true;
      }
      return uiXml.lastModified() > interfaceFile.lastModified();
    }

    private void generate() throws Exception {
      System.out.println(uiXml);
      handler = new UiXmlHandler();
      parser.parse(uiXml, handler);

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
        i.getMethod(uiField.name).returnType(config.getInterface(uiField.type));
      }

      for (final UiStyleDeclaration style : handler.styleFields) {
        i.getMethod(style.name).returnType(style.type);
      }

      save(i);
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
        new CssGenerator(style.getCssInFile(), style.type, output).run();
      }

      for (final UiFieldDeclaration field : handler.uiFields) {
        final String interfaceType = config.getInterface(field.type);
        final String subType = config.getSubclass(field.type);
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

      save(v);
    }

    private void generateStub() throws Exception {
      final GClass s = new GClass(stubName).baseClass(StubWidget.class).implementsInterface(interfaceName);

      final GMethod debugId = s.getMethod("setDebugId").argument("String", "baseDebugId");

      for (final UiFieldDeclaration field : handler.uiFields) {
        final String stubType = config.getStub(field.type);
        if (stubType == null) {
          throw new RuntimeException("No stub for " + field.type);
        }
        s.getField(field.name).type(stubType).setFinal().initialValue("new {}()", stubType);
        s.getMethod(field.name).returnType(stubType).body.line("return {};", field.name);
        debugId.body.line("{}.ensureDebugId(baseDebugId + \"-{}\");", field.name, field.name);
      }

      // Make fields, getter, plus StubCss class for each ui:style
      for (final UiStyleDeclaration style : handler.styleFields) {
        CssStubGenerator g = new CssStubGenerator(style.getCssInFile(), style.type, output);
        g.run();
        s.getField(style.name).type(style.type).setFinal().initialValue("new {}()", g.getCssStubClassName());
        s.getMethod(style.name).returnType(style.type).body.line("return {};", style.name);
      }

      s.getConstructor().body.line("setDebugId(\"{}\");", s.getSimpleClassNameWithoutGeneric().replaceAll("View$", "").replaceAll("^Stub", ""));

      save(s);
    }

    String getPath() {
      return uiXml.getPath();
    }

    List<UiFieldDeclaration> getWithTypes() {
      if (handler == null) {
        return cache.getWithTypes(this);
      } else {
        return handler.withFields;
      }
    }
  }

  private static class UiXmlHandler extends DefaultHandler {
    private final List<UiFieldDeclaration> withFields = new ArrayList<UiFieldDeclaration>();
    private final List<UiFieldDeclaration> uiFields = new ArrayList<UiFieldDeclaration>();
    private final List<UiStyleDeclaration> styleFields = new ArrayList<UiStyleDeclaration>();
    private String firstTagType;
    private UiStyleDeclaration lastStyle;

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
      if (firstTagType == null && uri.startsWith("urn:import")) {
        firstTagType = StringUtils.substringAfterLast(uri, ":") + "." + localName;
      }

      if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("with")) {
        final String type = attributes.getValue(attributes.getIndex("type"));
        final String name = attributes.getValue(attributes.getIndex("field"));
        withFields.add(new UiFieldDeclaration(type, name));
      }
      if (uri.equals("urn:ui:com.google.gwt.uibinder") && localName.equals("style")) {
        int fieldIndex = attributes.getIndex("field");
        int typeIndex = attributes.getIndex("type");
        if (typeIndex > -1) {
          final String name = fieldIndex == -1 ? "style" : attributes.getValue(fieldIndex);
          final String type = attributes.getValue(typeIndex);
          lastStyle = new UiStyleDeclaration(type, name);
          styleFields.add(lastStyle);
        }
      }

      final int indexOfUiField = attributes.getIndex("urn:ui:com.google.gwt.uibinder", "field");
      if (indexOfUiField > -1) {
        final String type;
        if (uri.equals("")) {
          type = Element.class.getName();
        } else {
          type = StringUtils.substringAfterLast(uri, ":") + "." + localName;
        }
        final String name = attributes.getValue(indexOfUiField);
        uiFields.add(new UiFieldDeclaration(type, name));
      }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
      if (lastStyle != null) {
        lastStyle.css += new String(ch, start, length);
      }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) {
      lastStyle = null;
    }
  }

  /** A DTO for {@code ui:with} or {@code ui:field} declarations. */
  static class UiFieldDeclaration implements Comparable<UiFieldDeclaration> {
    final String type;
    final String name;
    final boolean isElement;

    UiFieldDeclaration(final String type, final String name) {
      this.type = type;
      this.name = name;
      isElement = type.contains("dom");
    }

    @Override
    public int compareTo(final UiFieldDeclaration o) {
      return name.compareTo(o.name);
    }
  }

  /** A DTO for {@code ui:style} declarations. */
  static class UiStyleDeclaration {
    final String type;
    final String name;
    String css = "";

    UiStyleDeclaration(final String type, final String name) {
      this.type = type;
      this.name = name;
    }

    private File getCssInFile() {
      try {
        File f = File.createTempFile(name, ".tmp");
        FileUtils.writeStringToFile(f, css);
        f.deleteOnExit();
        return f;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

}
