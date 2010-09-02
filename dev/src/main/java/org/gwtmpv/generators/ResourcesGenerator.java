package org.gwtmpv.generators;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import joist.sourcegen.GClass;
import joist.sourcegen.GField;
import joist.sourcegen.GMethod;
import joist.util.Inflector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.gwtmpv.widgets.StubDataResource;
import org.gwtmpv.widgets.StubTextResource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.TextResource;

/** A utility class for generating resource interfaces from files in directory. */
public class ResourcesGenerator {

  /** Args: inputDirectory (src/com/app/resources), packageName (com.app.resources), outputDirectory (gen). */
  public static void main(final String[] args) throws Exception {
    final Map<String, String> settings = GenUtils.parseArgs(args);
    final File inputDirectory = new File(settings.get("inputDirectory"));
    final String packageName = settings.get("packageName");
    final File outputDirectory = new File(settings.get("outputDirectory"));
    new ResourcesGenerator(inputDirectory, packageName, outputDirectory).run();
  }

  private final File inputDirectory;
  private final String packageName;
  private final File outputDirectory;
  private final GClass appResources;
  private final GClass stubResources;

  public ResourcesGenerator(final File inputDirectory, final String packageName, final File outputDirectory) {
    this.inputDirectory = inputDirectory;
    this.packageName = packageName;
    this.outputDirectory = outputDirectory;
    appResources = new GClass(packageName + ".AppResources");
    stubResources = new GClass(packageName + ".StubAppResources");
  }

  public void run() throws Exception {
    appResources.setInterface().baseClass(ClientBundle.class);
    stubResources.implementsInterface(appResources.getFullClassName());

    for (final File file : getFilesInInputDirectory()) {
      if (file.isDirectory()) {
        continue;
      }

      if (file.getName().endsWith(".notstrict.css")) {
        addNotStrictCss(file);
      } else if (file.getName().endsWith(".css")) {
        addCss(file);
      } else if (file.getName().endsWith(".png") || file.getName().endsWith(".gif") || file.getName().endsWith(".jpg")) {
        addImage(file);
      } else if (file.getName().endsWith(".html") || file.getName().endsWith("js")) {
        addText(file);
      }
      System.out.println(file);
    }

    FileUtils.writeStringToFile(new File(outputDirectory, appResources.getFileName()), appResources.toCode());
    FileUtils.writeStringToFile(new File(outputDirectory, stubResources.getFileName()), stubResources.toCode());
  }

  private void addNotStrictCss(final File cssFile) throws Exception {
    final String methodName = GenUtils.toMethodName(cssFile.getName().replace(".notstrict.css", ""));
    final GMethod m = appResources.getMethod(methodName).returnType(CssResource.class);
    m.addAnnotation("@Source(\"" + getRelativePath(cssFile) + "\")");
    m.addAnnotation("@NotStrict");
    appResources.addImports(NotStrict.class.getName().replace("$", "."));

    stubResources.getMethod(methodName).returnType(CssResource.class).body.line("return null;");
  }

  private void addCss(final File cssFile) throws Exception {
    final String methodName = GenUtils.toMethodName(cssFile.getName().replace(".css", ""));
    final String newInterfaceName = packageName + "." + Inflector.capitalize(methodName);
    final String stubName = packageName + ".Stub" + Inflector.capitalize(methodName);

    final GMethod m = appResources.getMethod(methodName).returnType(newInterfaceName);
    m.addAnnotation("@Source(\"" + getRelativePath(cssFile) + "\")");

    // stub
    final GField sf = stubResources.getField(methodName).type(stubName).setFinal();
    sf.initialValue("new {}()", stubName).autoImportInitialValue();
    stubResources.getMethod(methodName).returnType(stubName).body.line("return {};", methodName);

    new CssGenerator(cssFile, newInterfaceName, outputDirectory).run();
    new CssStubGenerator(cssFile, newInterfaceName, outputDirectory).run();
  }

  public void addImage(final File imageFile) throws Exception {
    final String methodName = GenUtils.toMethodName(StringUtils.substringBeforeLast(imageFile.getName(), "."));

    final GMethod m = appResources.getMethod(methodName).returnType(DataResource.class);
    m.addAnnotation("@Source(\"{}\")", getRelativePath(imageFile));

    // stub
    final GField sf = stubResources.getField(methodName).type(DataResource.class).setFinal();
    sf.initialValue("new StubDataResource(\"{}\", \"{}\")", methodName, imageFile.getName());
    stubResources.getMethod(methodName).returnType(DataResource.class).body.line("return {};", methodName);
    stubResources.addImports(StubDataResource.class);
  }

  public void addText(final File textFile) throws Exception {
    final String methodName = GenUtils.toMethodName(StringUtils.substringBeforeLast(textFile.getName(), "."));

    final GMethod m = appResources.getMethod(methodName).returnType(TextResource.class);
    m.addAnnotation("@Source(\"{}\")", getRelativePath(textFile));

    // stub
    final GField sf = stubResources.getField(methodName).type(TextResource.class).setFinal();
    sf.initialValue("new StubTextResource(\"{}\", \"{}\")", methodName, textFile.getName());
    stubResources.getMethod(methodName).returnType(TextResource.class).body.line("return {};", methodName);
    stubResources.addImports(StubTextResource.class);
  }

  private String getRelativePath(File file) {
    return file.getAbsolutePath().replace(inputDirectory.getAbsolutePath() + "/", "");
  }

  @SuppressWarnings("unchecked")
  private Collection<File> getFilesInInputDirectory() {
    String[] exts = new String[] { "css", "png", "gif", "jpg", "html", "js" };
    return FileUtils.listFiles(inputDirectory, exts, true);
  }

}
