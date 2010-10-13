package org.gwtmpv.generators.views;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Join;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

/** Takes a {@code ui.xml} source and generates a {@code IsXxx, GwtXxx, StubXxx} trio of view classes. */
public class ViewGenerator {

  private final String packageName;
  private final List<UiXmlFile> uiXmlFiles = new ArrayList<UiXmlFile>();
  final File input;
  final File output;
  final Config config = new Config();
  final UiXmlCache cache;
  final SAXParser parser;

  public ViewGenerator(final File inputDirectory, final String packageName, final File outputDirectory) {
    input = inputDirectory.getAbsoluteFile();
    output = outputDirectory.getAbsoluteFile();
    cache = new UiXmlCache(output);
    this.packageName = packageName;
    parser = makeNewParser();
  }

  public void generate() throws Exception {
    cache.loadIfExists();
    for (final File uiXml : findUiXmlFiles()) {
      if (uiXml.getName().contains("-nogen.")) {
        continue;
      }
      uiXmlFiles.add(new UiXmlFile(this, uiXml));
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
  }

  private void generateAppViews() {
    final GClass appViews = new GClass(packageName + ".AppViews").setInterface();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      appViews.getMethod("new" + uiXml.simpleName).returnType(uiXml.interfaceName);
    }
    save(appViews);
  }

  private void generateGwtViews() {
    final GClass gwtViews = new GClass(packageName + ".GwtViews").implementsInterface("AppViews");
    final GMethod cstr = gwtViews.getConstructor();

    // ui:withs in separate files could use the same type but different
    // variable names, so we resolve based on the type only
    for (String type : getUniqueWithTypes()) {
      final String name = resourceName(type);
      gwtViews.getField(name).type(type).setFinal();
      cstr.argument(type, name);
      cstr.body.line("this.{} = {};", name, name);
    }

    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = gwtViews.getMethod("new" + uiXml.simpleName).returnType(uiXml.interfaceName);
      final List<String> withFieldNames = new ArrayList<String>();
      for (final UiFieldDeclaration with : uiXml.getWithTypes()) {
        withFieldNames.add("this." + resourceName(with.type));
      }
      m.addAnnotation("@Override");
      m.body.line("return new {}({});", uiXml.gwtName, Join.commaSpace(withFieldNames));
    }
    save(gwtViews);
  }

  private void generateStubViews() {
    final GClass stubViews = new GClass(packageName + ".StubViews").implementsInterface("AppViews");
    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = stubViews.getMethod("new" + uiXml.simpleName).returnType(uiXml.stubName);
      m.addAnnotation("@Override");
      m.body.line("return new {}();", uiXml.stubName);
    }
    save(stubViews);
  }

  /** @return the unique types required by {@code ui:with}s across all views */
  private Set<String> getUniqueWithTypes() {
    final Set<String> all = new TreeSet<String>();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      for (final UiFieldDeclaration field : uiXml.getWithTypes()) {
        all.add(field.type);
      }
    }
    return all;
  }

  private String resourceName(String fullName) {
    return StringUtils.uncapitalize(StringUtils.substringAfterLast(fullName, "."));
  }

  void save(final GClass gclass) {
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

}
