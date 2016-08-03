package org.tessell.generators.resources;

import static joist.sourcegen.Argument.arg;
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
import org.tessell.generators.Cleanup;
import org.tessell.generators.GenUtils;
import org.tessell.generators.css.CssGenerator;
import org.tessell.generators.css.CssStubGenerator;
import org.tessell.gwt.resources.client.StubDataResource;
import org.tessell.gwt.resources.client.StubImageResource;
import org.tessell.gwt.resources.client.StubTextResource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

/** A utility class for generating resource interfaces from files in directory. */
public class ResourcesGenerator {

  // ignore literal('url(...)') with a negative look behind
  private static final Pattern urlPattern = Pattern.compile("(?<!literal\\(')url\\(([^\\)]+)\\)");
  private static final Pattern plusKeywordPattern = Pattern.compile("\\-X-([a-z-]+): *([^;]+);");
  private final File inputDirectory;
  private final Cleanup cleanup;
  private final String packageName;
  private final File outputDirectory;
  private final GClass appResources;
  private final GClass stubResources;
  private final GClass appResourcesUtil;
  private final GMethod injectAll;

  public ResourcesGenerator(final File inputDirectory, Cleanup cleanup, final String packageName, final File outputDirectory) {
    this.inputDirectory = inputDirectory;
    this.cleanup = cleanup;
    this.packageName = packageName;
    this.outputDirectory = outputDirectory;
    appResources = new GClass(packageName + ".AppResources");
    stubResources = new GClass(packageName + ".StubAppResources");
    appResourcesUtil = new GClass(packageName + ".AppResourcesUtil");
    injectAll = appResourcesUtil.getMethod("injectAll", arg("AppResources", "r")).setStatic();
  }

  public void run() throws Exception {
    appResources.setInterface().baseClass(ClientBundle.class);
    stubResources.implementsInterface(appResources.getFullName());

    for (final File file : getFilesInInputDirectory()) {
      if (file.getName().endsWith(".css")) {
        addCss(file);
      } else if (file.getName().endsWith(".png")
        || file.getName().endsWith(".gif")
        || file.getName().endsWith(".jpg")
        || file.getName().endsWith(".bmp")
        || file.getName().endsWith("htc")) {
        addImage(file);
      } else if (file.getName().endsWith(".html") || file.getName().endsWith("js")) {
        addText(file);
      }
    }

    cleanup.markOkay(appResources);
    cleanup.markOkay(stubResources);
    cleanup.markOkay(appResourcesUtil);
    GenUtils.saveIfChanged(outputDirectory, appResources);
    GenUtils.saveIfChanged(outputDirectory, stubResources);
    GenUtils.saveIfChanged(outputDirectory, appResourcesUtil);
  }

  private void addCss(final File cssFile) throws Exception {
    final String methodName = GenUtils.toMethodName(cssFile.getName().replace(".css", "").replace(".notstrict", ""));
    final String newInterfaceName = packageName + "." + suffixIfNeeded(Inflector.capitalize(methodName), "Style");
    final String stubName = packageName + ".Stub" + suffixIfNeeded(Inflector.capitalize(methodName), "Style");

    // Copy the file and do any url(...) => @url replacement
    File cssFileCopy = fileInOutputDirectory(inputDirectory, outputDirectory, cssFile, ".css", ".gen.css");
    String cssContent = FileUtils.readFileToString(cssFile);
    cssContent = doUrlSubstitution(cssContent);
    cssContent = doMozWebkitSubstitution(cssContent);
    GenUtils.saveIfChanged(cssFileCopy, cssContent);
    cleanup.markOkay(cssFileCopy);

    final GMethod m = appResources.getMethod(methodName).returnType(newInterfaceName);
    m.addAnnotation("@Source(\"" + getRelativePath(cssFileCopy) + "\")");

    boolean notStrict = cssFile.getName().endsWith(".notstrict.css");
    if (notStrict) {
      m.addAnnotation("@NotStrict");
      appResources.addImports(NotStrict.class.getName().replace("$", "."));
    }

    // stub
    final GField sf = stubResources.getField(methodName).type(stubName).setFinal();
    sf.initialValue("new {}()", stubName).autoImportInitialValue();
    stubResources.getMethod(methodName).returnType(stubName).body.line("return {};", methodName);

    new CssGenerator(cssFile, cleanup, newInterfaceName, outputDirectory).run();
    new CssStubGenerator(cssFile, cleanup, newInterfaceName, outputDirectory).run();

    // inject all
    injectAll.body.line("r.{}().ensureInjected();", methodName);
  }

  private String suffixIfNeeded(String name, String suffix) {
    return name.endsWith(suffix) ? name : name + suffix;
  }

  /** @return a file in the same package as {@code file} but in the output (generated) directory */
  public static File fileInOutputDirectory(File inputDirectory, File outputDirectory, File file, String extMatch, String extReplace) {
    return new File(//
      file.getAbsolutePath()//
      .replace(inputDirectory.getAbsolutePath(), outputDirectory.getAbsolutePath())
      .replace(extMatch, extReplace));
  }

  /** Finds {@code url(...)} in the css and replaces it with GWT @url declarations. */
  private static String doUrlSubstitution(String cssContent) throws Exception {
    // keep track of urls we've already defined so we don't redefine them
    Set<String> defined = new HashSet<String>();
    // buffer for @url definitions, to be prepended at the end
    StringBuffer defs = new StringBuffer();
    // buffer to hold the transformed css, to be appended at the end
    StringBuffer sb = new StringBuffer();
    Matcher m = urlPattern.matcher(cssContent);
    while (m.find()) {
      String path = m.group(1);
      String methodName = GenUtils.toMethodName(substringBeforeLast(new File(path).getName(), "."));
      String aliasName = "_" + methodName + "Url";
      String resourceName = methodName + "Data"; // for the DataResource
      if (defined.add(aliasName)) {
        defs.append("@url " + aliasName + " " + resourceName + ";\n");
      }
      m.appendReplacement(sb, aliasName);
    }
    m.appendTail(sb);
    return defs.toString() + sb.toString();
  }

  public static String doMozWebkitSubstitution(String cssContent) {
    StringBuffer sb = new StringBuffer();
    Matcher m = plusKeywordPattern.matcher(cssContent);
    while (m.find()) {
      String property = m.group(1);
      String value = m.group(2);
      String newValue = "";
      for (String prefix : new String[] { "-moz-", "-webkit-", "-o-", "-ms-", "" }) {
        newValue = newValue + prefix + property + ": " + value + "; ";
      }
      m.appendReplacement(sb, newValue.trim());
    }
    m.appendTail(sb);
    return sb.toString();
  }

  public void addImage(final File imageFile) throws Exception {
    final String methodName = GenUtils.toMethodName(StringUtils.substringBeforeLast(imageFile.getName(), "."));
    {
      // ImageResource
      appResources.getMethod(methodName) //
      .returnType(ImageResource.class)
      .addAnnotation("@Source(\"{}\")", getRelativePath(imageFile));
      // stub
      stubResources.getField(methodName) //
      .type(ImageResource.class)
      .setFinal()
      .initialValue("new StubImageResource(\"{}\", \"{}\")", methodName, imageFile.getName());
      stubResources.getMethod(methodName).returnType(ImageResource.class).body.line("return {};", methodName);
    }
    {
      // DataResource
      appResources.getMethod(methodName + "Data") //
      .returnType(DataResource.class)
      .addAnnotation("@Source(\"{}\")", getRelativePath(imageFile));
      // stub
      stubResources.getField(methodName + "Data") //
      .type(DataResource.class)
      .setFinal()
      .initialValue("new StubDataResource(\"{}\", \"{}\")", methodName, imageFile.getName());
      stubResources.getMethod(methodName + "Data").returnType(DataResource.class).body.line("return {};", methodName + "Data");
    }
    stubResources.addImports(StubImageResource.class, StubDataResource.class);
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
    String path = absolutePath(file);
    String inputDirectoryPath = absolutePath(inputDirectory);
    String relativePath;
    if (path.contains(inputDirectoryPath)) {
      relativePath = path.replace(inputDirectoryPath + "/" + packageName.replace(".", "/") + "/", "");
    } else {
      String outputDirectoryPath = absolutePath(outputDirectory);
      relativePath = path.replace(outputDirectoryPath + "/" + packageName.replace(".", "/") + "/", "");
    }
    return relativePath;
  }

  private String absolutePath(File file) {
    return slashSanitise(file.getAbsolutePath());
  }

  private String slashSanitise(String value) {
    return value.replaceAll("\\\\", "/");
  }

  private Collection<File> getFilesInInputDirectory() {
    String[] exts = new String[] { "css", "png", "gif", "jpg", "html", "js", "htc" };
    File packageDirectory = new File(inputDirectory, packageName.replace(".", File.separator));
    return FileUtils.listFiles(packageDirectory, exts, true);
  }

}
