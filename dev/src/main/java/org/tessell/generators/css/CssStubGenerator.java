package org.tessell.generators.css;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBeforeLast;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import joist.sourcegen.GClass;

import org.tessell.generators.Cleanup;

import com.google.gwt.resources.css.ast.CssProperty.Value;

/** A utility class for creating a Java stub class for a given CSS file. */
public class CssStubGenerator extends AbstractCssGenerator {

  private final GClass cssStub;

  public CssStubGenerator(final File inputCssFile, Cleanup cleanup, final String interfaceName, final File outputDirectory) {
    super(inputCssFile, outputDirectory, cleanup);

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
    return cssStub.getFullName();
  }

  private void generateStub() {
    for (final Map.Entry<String, String> e : getClassNameToMethodName().entrySet()) {
      final String className = e.getKey();
      final String methodName = e.getValue();
      if (className.equals("*")) {
        continue; // skip @external *; declarations
      }
      cssStub.getMethod(methodName).returnType(String.class).body.line("return \"{}\";", methodName);
    }
    for (final Map.Entry<String, Value> def : getDefs().entrySet()) {
      // need stricter matching
      if (def.getValue().toString().endsWith("px")) {
        cssStub.getMethod(def.getKey()).returnType(int.class).body.line("return {};", def.getValue().toString().replace("px", ""));
      }
    }

    cssStub.getMethod("ensureInjected").returnType("boolean").body.line("return true;");
    cssStub.getMethod("getText").returnType("String").body.line("return null;");
    cssStub.getMethod("getName").returnType("String").body.line("return null;");
  }
}
