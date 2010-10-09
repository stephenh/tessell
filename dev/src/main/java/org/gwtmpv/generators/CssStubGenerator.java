package org.gwtmpv.generators;

import java.io.File;
import java.io.IOException;

import joist.sourcegen.GClass;

import org.apache.commons.io.FileUtils;

/** A utility class for creating a Java stub class for a given CSS file. */
public class CssStubGenerator extends AbstractCssGenerator {

  /**
   * Args: css file, stub file name
   */
  public static void main(final String[] args) {
  }

  private final File inputCssFile;
  private final File outputDirectory;
  private final GClass cssStub;

  public CssStubGenerator(final File inputCssFile, final String interfaceName, final File outputDirectory) {
    this.inputCssFile = inputCssFile;
    this.outputDirectory = outputDirectory;

    // break open interfaceName and put Stub in the front of its simple name
    final int lastDot = interfaceName.lastIndexOf('.');
    final String packageName = interfaceName.substring(0, lastDot);
    final String interfaceSimpleName = interfaceName.substring(lastDot + 1);
    cssStub = new GClass(packageName + ".Stub" + interfaceSimpleName).implementsInterface(interfaceName);
  }

  public void run() throws IOException {
    generateStub();
    FileUtils.writeStringToFile(new File(outputDirectory, cssStub.getFileName()), cssStub.toCode());
  }

  public String getCssStubClassName() {
    return cssStub.getFullClassName();
  }

  private void generateStub() {
    for (final String methodName : getClassNameToMethodName(inputCssFile).values()) {
      cssStub.getMethod(methodName).returnType(String.class).body.line("return \"{}\";", methodName);
    }

    cssStub.getMethod("ensureInjected").returnType("boolean").body.line("return true;");
    cssStub.getMethod("getText").returnType("String").body.line("return null;");
    cssStub.getMethod("getName").returnType("String").body.line("return null;");
  }

}
