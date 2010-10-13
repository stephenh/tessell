package org.gwtmpv.generators.views;

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

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Join;

import org.apache.commons.io.FileUtils;
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
    for (final UiFieldDeclaration with : getWithsFromAllViews()) {
      gwtViews.getField(with.name).type(with.type).setFinal();
      cstr.argument(with.type, with.name);
      cstr.body.line("this.{} = {};", with.name, with.name);
    }
    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = gwtViews.getMethod("new" + uiXml.simpleName).returnType(uiXml.interfaceName);
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
      final GMethod m = stubViews.getMethod("new" + uiXml.simpleName).returnType(uiXml.stubName);
      m.addAnnotation("@Override");
      m.body.line("return new {}();", uiXml.stubName);
    }
    save(stubViews);
  }

  private Collection<UiFieldDeclaration> getWithsFromAllViews() {
    final Map<String, UiFieldDeclaration> map = new HashMap<String, UiFieldDeclaration>();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      for (final UiFieldDeclaration field : uiXml.getWithTypes()) {
        map.put(field.name, field);
      }
    }
    return new TreeSet<UiFieldDeclaration>(map.values());
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
