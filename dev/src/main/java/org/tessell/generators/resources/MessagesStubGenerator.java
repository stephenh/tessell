package org.tessell.generators.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Set;

import org.apache.tapestry.util.text.LocalizedProperties;

import com.google.gwt.dev.util.Util;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generates stub implementations of Messages interfaces.
 * 
 * Should this use the default values of the messages instead of just the key name?
 */
public class MessagesStubGenerator {

  public static void main(final String[] args) throws Exception {
    // this is horribly hard-coded, but works for us
    final String sourceDir = args[0];
    final String fullName = args[1];
    final File resourceBundle = urlToResourceFile(fullName);

    final int lastDot = fullName.lastIndexOf(".");
    final String packageName = fullName.substring(0, lastDot);
    final String simpleName = fullName.substring(lastDot + 1);

    final File sourcePath = new File(new File(sourceDir).getCanonicalFile()
      + File.separator
      + packageName.replace('.', File.separatorChar)
      + File.separator
      + "Stub"
      + simpleName
      + ".java");

    new MessagesStubGenerator(simpleName, "Stub" + simpleName, packageName, resourceBundle, sourcePath).generate();
  }

  private final File resourceBundle;
  private final SourceWriter composer;

  public MessagesStubGenerator(
    final String className,
    final String stubName,
    final String packageName,
    final File resourceBundle,
    final File targetLocation) throws IOException {
    this.resourceBundle = resourceBundle;

    final ClassSourceFileComposerFactory factory = new ClassSourceFileComposerFactory(packageName, stubName);
    factory.addImplementedInterface(packageName + "." + className);

    final FileOutputStream file = new FileOutputStream(targetLocation);
    final Writer underlying = new OutputStreamWriter(file, Util.DEFAULT_ENCODING);
    composer = factory.createSourceWriter(new PrintWriter(underlying));
  }

  @SuppressWarnings("unchecked")
  public void generate() throws IOException {
    final InputStream propStream = new FileInputStream(resourceBundle);
    final LocalizedProperties p = new LocalizedProperties();
    p.load(propStream, Util.DEFAULT_ENCODING);

    // sort keys for deterministic results
    final Set<String> keySet = p.getPropertyMap().keySet();
    final String[] keys = keySet.toArray(new String[keySet.size()]);
    Arrays.sort(keys);

    for (final String key : keys) {
      final String value = p.getProperty(key);
      // not doing dup/replace detection because they are private in the super class
      addMethod(key, value);
    }

    composer.commit(new PrintWriterTreeLogger());
  }

  public void addMethod(final String key, final String defaultValue) {
    composer.indent();
    composer.println("public String " + key + "() {");
    composer.indentln("return \"" + key + "\";");
    composer.println("}");
    composer.outdent();
    composer.println();
  }

  // copy/paste from GWT
  private static File urlToResourceFile(final String className) throws IOException {
    if (className.endsWith(".java") || className.endsWith(".properties") || className.endsWith(".class") || className.indexOf(File.separator) > 0) {
      throw new IllegalArgumentException("class '"
        + className
        + "'should not contain an extension. \"com.google.gwt.SomeClass\" is an example of a correctly formed class string");
    }
    final String resourcePath = className.replace('.', '/') + ".properties";
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    if (cl == null) {
      cl = ClassLoader.getSystemClassLoader();
    }
    final URL r = cl.getResource(resourcePath);
    if (r == null) {
      throw new FileNotFoundException("Could not find the resource '"
        + resourcePath
        + " matching '"
        + className
        + "' did you remember to add it to your classpath?");
    }
    final File resourceFile = new File(URLDecoder.decode(r.getPath(), "utf-8"));
    return resourceFile;
  }

}
