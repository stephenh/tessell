package org.tessell.generators.views;

import static joist.sourcegen.Argument.arg;

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

import joist.sourcegen.Argument;
import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;
import joist.util.Join;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.tessell.generators.Cleanup;
import org.tessell.generators.GenUtils;
import org.xml.sax.SAXException;

/** Takes a {@code ui.xml} source and generates a {@code IsXxx, GwtXxx, StubXxx} trio of view classes. */
public class ViewGenerator {

  private final String packageName;
  private final List<UiXmlFile> uiXmlFiles = new ArrayList<UiXmlFile>();
  private final UiXmlCache cache;
  final File input;
  final File output;
  final Cleanup cleanup;
  final Config config = new Config();
  final SAXParser parser;

  public ViewGenerator(final File inputDirectory, final String packageName, final File outputDirectory, final Cleanup cleanup) {
    input = inputDirectory.getAbsoluteFile();
    output = outputDirectory.getAbsoluteFile();
    cache = UiXmlCache.loadOrCreate(output);
    this.packageName = packageName;
    this.cleanup = cleanup;
    parser = makeNewParser();
  }

  public void generate() throws Exception {
    for (final File uiXml : findUiXmlFiles()) {
      if (uiXml.getName().contains("-nogen.")) {
        continue;
      }
      uiXmlFiles.add(new UiXmlFile(this, uiXml));
    }

    boolean viewgenChanged = !config.getViewgenTimestamp().equals(cache.getViewgenTimestamp());

    for (final UiXmlFile uiXml : uiXmlFiles) {
      if (uiXml.hasChanged() || !cache.has(uiXml) || viewgenChanged) {
        uiXml.generate();
        cache.update(uiXml);
      }
      cleanup.markOkay(uiXml.isView);
      cleanup.markOkay(uiXml.gwtView);
      cleanup.markOkay(uiXml.stubView);
      cleanup.markOkay(uiXml.uiXmlCopy);
      for (final UiStyleDeclaration style : uiXml.getPossiblyCachedStyles(cache)) {
        cleanup.markTypeOkay(style.type);
        cleanup.markTypeOkay(style.getStubClassName());
      }
    }

    generateAppViews();
    generateAppViewsProvider();
    generateGwtViews();
    generateStubViews();

    cache.save(output, config.getViewgenTimestamp());
  }

  private void generateAppViews() {
    final GClass appViews = new GClass(packageName + ".AppViews");

    for (final UiXmlFile uiXml : uiXmlFiles) {
      GMethod m = appViews.getMethod("new" + uiXml.baseName).returnType(uiXml.isView.getFullName()).setStatic();
      m.body.line("return provider.new{}();", uiXml.baseName);
    }

    appViews.getField("provider").setStatic().type("AppViewsProvider");
    GMethod m = appViews.getMethod("setProvider", arg("AppViewsProvider", "provider")).setStatic();
    m.body.line("AppViews.provider = provider;");

    markAndSaveIfChanged(appViews);
  }

  private void generateAppViewsProvider() {
    final GClass appViewsProvider = new GClass(packageName + ".AppViewsProvider").setInterface();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      appViewsProvider.getMethod("new" + uiXml.baseName).returnType(uiXml.isView.getFullName());
    }
    markAndSaveIfChanged(appViewsProvider);
  }

  private void generateGwtViews() {
    final GClass gwtViews = new GClass(packageName + ".GwtViewsProvider").implementsInterface("AppViewsProvider");
    final GMethod cstr = gwtViews.getConstructor();

    // ui:withs in separate files could use the same type but different
    // variable names, so we resolve based on the type only
    for (String type : getUniqueWithTypes()) {
      final String name = simpleName(type);
      gwtViews.getField(name).type(type).setFinal();
      cstr.argument(type, name);
      cstr.body.line("this.{} = {};", name, name);
    }

    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = gwtViews.getMethod("new" + uiXml.baseName).returnType(uiXml.isView.getFullName());
      final List<String> withFieldNames = new ArrayList<String>();
      for (final UiWithDeclaration with : uiXml.getPossiblyCachedWiths(cache)) {
        withFieldNames.add("this." + simpleName(with.type));
      }
      m.addAnnotation("@Override");
      m.body.line("return new {}({});", uiXml.gwtView.getFullName(), Join.commaSpace(withFieldNames));
    }
    markAndSaveIfChanged(gwtViews);
  }

  private void generateStubViews() {
    final GClass stubViews = new GClass(packageName + ".StubViewsProvider").implementsInterface("AppViewsProvider");

    // aggregate stub dependencies across all the files
    List<Argument> cstrArgs = new ArrayList<Argument>();
    List<String> cstrNames = new ArrayList<String>();
    for (String type : getUniqueStubDependencies()) {
      final String name = simpleName(type);
      stubViews.getField(name).type(type).setFinal();
      cstrArgs.add(arg(type, name));
      cstrNames.add(name);
    }
    // ui:withs in separate files could use the same type but different
    // variable names, so we resolve based on the type only
    for (String type : getUniqueWithTypes()) {
      final String name = simpleName(type);
      if (!cstrNames.contains(name)) {
        stubViews.getField(name).type(type).setFinal();
        cstrArgs.add(arg(type, name));
        cstrNames.add(name);
      }
    }

    stubViews.getConstructor(cstrArgs).assignFields();

    for (final UiXmlFile uiXml : uiXmlFiles) {
      final GMethod m = stubViews.getMethod("new" + uiXml.baseName).returnType(uiXml.stubView.getFullName());
      m.addAnnotation("@Override");
      // look for stub dependencies, sort by simple name
      Set<String> stubArgs = new TreeSet<String>();
      for (String type : uiXml.getPossiblyCachedStubDependencies(cache)) {
        stubArgs.add(simpleName(type));
      }
      for (UiWithDeclaration uiWith : uiXml.getPossiblyCachedWiths(cache)) {
        stubArgs.add(simpleName(uiWith.type));
      }
      m.body.line("return new {}({});", uiXml.stubView.getFullName(), Join.commaSpace(stubArgs));
    }

    final GMethod i = stubViews.getMethod("install", cstrArgs).setStatic();
    i.body.line("AppViews.setProvider(new StubViewsProvider({}));", Join.commaSpace(cstrNames));

    markAndSaveIfChanged(stubViews);
  }

  /** @return the unique types required by {@code ui:with}s across all views */
  private Set<String> getUniqueWithTypes() {
    final Set<String> all = new TreeSet<String>();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      for (final UiWithDeclaration field : uiXml.getPossiblyCachedWiths(cache)) {
        all.add(field.type);
      }
    }
    return all;
  }

  /** @return the unique stub dependencies across all views */
  private Set<String> getUniqueStubDependencies() {
    final Set<String> all = new TreeSet<String>();
    for (final UiXmlFile uiXml : uiXmlFiles) {
      all.addAll(uiXml.getPossiblyCachedStubDependencies(cache));
    }
    return all;
  }

  void markAndSaveIfChanged(final GClass gclass) {
    GenUtils.saveIfChanged(output, gclass);
    cleanup.markOkay(gclass);
  }

  /** If the ui.xml files have had their time stamp changed, we always save the new ones. */
  void markAndSave(final GClass gclass) {
    cleanup.markOkay(gclass);
    try {
      FileUtils.writeStringToFile(new File(output, gclass.getFileName()), gclass.toCode());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Collection<File> findUiXmlFiles() {
    return FileUtils.listFiles(input, new String[] { "ui.xml" }, true);
  }

  private static SAXParser makeNewParser() {
    final SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating(false);
    factory.setNamespaceAware(true);
    try {
      // don't hit the net for DTDs and crap
      // http://stackoverflow.com/questions/243728/how-to-disable-dtd-at-runtime-in-javas-xpath
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
      factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      return factory.newSAXParser();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
  }

  protected static String simpleName(String fullName) {
    return StringUtils.uncapitalize(StringUtils.substringAfterLast(fullName, "."));
  }

}
