package org.gwtmpv.generators.resources;

import static org.apache.commons.lang.StringUtils.substringBeforeLast;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import joist.sourcegen.GClass;
import joist.sourcegen.GField;
import joist.sourcegen.GMethod;
import joist.util.Inflector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.gwtmpv.generators.GenUtils;
import org.gwtmpv.generators.css.CssGenerator;
import org.gwtmpv.generators.css.CssStubGenerator;
import org.gwtmpv.widgets.StubDataResource;
import org.gwtmpv.widgets.StubTextResource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.TextResource;

/** A utility class for generating resource interfaces from files in directory. */
public class ResourcesGenerator {

  private static final Pattern urlPattern = Pattern.compile("url\\(([^\\)]+)\\)");
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
      if (file.getName().endsWith(".notstrict.css")) {
        addCss(file, true);
      } else if (file.getName().endsWith(".css")) {
        addCss(file, false);
      } else if (file.getName().endsWith(".png")
        || file.getName().endsWith(".gif")
        || file.getName().endsWith(".jpg")
        || file.getName().endsWith("htc")) {
        addImage(file);
      } else if (file.getName().endsWith(".html") || file.getName().endsWith("js")) {
        addText(file);
      }
    }

    GenUtils.saveIfChanged(outputDirectory, appResources);
    GenUtils.saveIfChanged(outputDirectory, stubResources);
  }

  private void addCss(final File cssFile, boolean notStrict) throws Exception {
    final String methodName = GenUtils.toMethodName(cssFile.getName().replace(".css", "").replace(".notstrict", ""));
    final String newInterfaceName = packageName + "." + suffixIfNeeded(Inflector.capitalize(methodName), "Style");
    final String stubName = packageName + ".Stub" + suffixIfNeeded(Inflector.capitalize(methodName), "Style");

    // Copy the file and do any url(...) => @url replacement
    File cssFileCopy = fileInOutputDirectory(cssFile, ".css", ".gen.css");
    doUrlSubstitution(cssFile, cssFileCopy);

    final GMethod m = appResources.getMethod(methodName).returnType(newInterfaceName);
    m.addAnnotation("@Source(\"" + getRelativePath(cssFileCopy) + "\")");
    if (notStrict) {
      m.addAnnotation("@NotStrict");
      appResources.addImports(NotStrict.class.getName().replace("$", "."));
    }

    // stub
    final GField sf = stubResources.getField(methodName).type(stubName).setFinal();
    sf.initialValue("new {}()", stubName).autoImportInitialValue();
    stubResources.getMethod(methodName).returnType(stubName).body.line("return {};", methodName);

    new CssGenerator(cssFile, newInterfaceName, outputDirectory).run();
    new CssStubGenerator(cssFile, newInterfaceName, outputDirectory).run();
  }

  private String suffixIfNeeded(String name, String suffix) {
    return name.endsWith(suffix) ? name : name + suffix;
  }

  /** @return a file in the same package as {@code file} but in the output (generated) directory */
  private File fileInOutputDirectory(File file, String extMatch, String extReplace) {
    return new File(//
      file.getAbsolutePath()//
        .replace(inputDirectory.getAbsolutePath(), outputDirectory.getAbsolutePath())
        .replace(extMatch, extReplace));
  }

  /** Finds {@code url(...)} in the css and replaces it with GWT @url declarations. */
  private void doUrlSubstitution(File original, File gen) throws Exception {
    // keep track of urls we've already defined so we don't redefine them
    Set<String> defined = new HashSet<String>();
    // buffer for @url definitions, to be prepended at the end
    StringBuffer defs = new StringBuffer();
    // buffer to hold the transformed css, to be appended at the end
    StringBuffer sb = new StringBuffer();

    Matcher m = urlPattern.matcher(FileUtils.readFileToString(original));
    while (m.find()) {
      String path = m.group(1);
      String methodName = GenUtils.toMethodName(substringBeforeLast(new File(path).getName(), "."));
      String aliasName = methodName + "Url";
      if (defined.add(aliasName)) {
        defs.append("@url " + aliasName + " " + methodName + ";\n");
      }
      m.appendReplacement(sb, aliasName);
    }
    m.appendTail(sb);

    String newContent = defs.toString() + sb.toString();
    String oldContent = FileUtils.readFileToString(gen);
    if (!newContent.equals(oldContent)) {
      FileUtils.writeStringToFile(gen, newContent);
      System.out.println(gen.getName());
    }
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
    String path = file.getAbsolutePath();
    if (path.contains(inputDirectory.getAbsolutePath())) {
      return path.replace(inputDirectory.getAbsolutePath() + "/" + packageName.replace(".", "/") + "/", "");
    } else {
      return path.replace(outputDirectory.getAbsolutePath() + "/" + packageName.replace(".", "/") + "/", "");
    }
  }

  @SuppressWarnings("unchecked")
  private Collection<File> getFilesInInputDirectory() {
    String[] exts = new String[] { "css", "png", "gif", "jpg", "html", "js", "htc" };
    File packageDirectory = new File(inputDirectory, packageName.replace(".", "/"));
    return FileUtils.listFiles(packageDirectory, exts, true);
  }

}
