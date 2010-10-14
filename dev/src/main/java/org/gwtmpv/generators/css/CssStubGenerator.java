package org.gwtmpv.generators.css;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBeforeLast;

import java.io.File;
import java.io.IOException;

import joist.sourcegen.GClass;

import org.gwtmpv.generators.Cleanup;

/** A utility class for creating a Java stub class for a given CSS file. */
public class CssStubGenerator extends AbstractCssGenerator {

  private final File inputCssFile;
  private final GClass cssStub;

  public CssStubGenerator(final File inputCssFile, Cleanup cleanup, final String interfaceName, final File outputDirectory) {
    super(outputDirectory, cleanup);
    this.inputCssFile = inputCssFile;

    // break open interfaceName and put Stub in the front of its simple name
    final String packageName = substringBeforeLast(interfaceName, ".");
    final String simpleName = substringAfterLast(interfaceName, ".");
    cssStub = new GClass(packageName + ".Stub" + simpleName).implementsInterface(interfaceName);
  }

  public void run() throws IOException {
    generateStub();
    markAndSaveIfChanged(cssStub);
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
