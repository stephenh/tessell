package org.gwtmpv.generators;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;

import org.apache.commons.io.FileUtils;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.resources.client.CssResource;

/** A utility class for creating a Java interface declaration for a given CSS file. */
public class CssGenerator extends AbstractCssGenerator {

  /**
   * Args: css file, stub file name
   */
  public static void main(final String[] args) {
  }

  private final File inputCssFile;
  private final File outputDirectory;
  private final GClass cssInterface;

  public CssGenerator(final File inputCssFile, final String interfaceName, final File outputDirectory) {
    this.inputCssFile = inputCssFile;
    this.outputDirectory = outputDirectory;
    cssInterface = new GClass(interfaceName).setInterface().baseClass(CssResource.class);
  }

  public void run() throws IOException {
    addMethods();
    System.out.println(cssInterface.getFileName());
    FileUtils.writeStringToFile(new File(outputDirectory, cssInterface.getFileName()), cssInterface.toCode());
  }

  private void addMethods() {
    for (final Map.Entry<String, String> e : getClassNameToMethodName(inputCssFile).entrySet()) {
      final String className = e.getKey();
      final String methodName = e.getValue();
      final GMethod m = cssInterface.getMethod(methodName).returnType(String.class);
      if (!methodName.equals(className)) {
        m.addAnnotation("@ClassName(\"{}\")", Generator.escape(className));
      }
    }
  }
}
