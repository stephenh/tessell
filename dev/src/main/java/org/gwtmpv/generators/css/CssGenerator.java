package org.gwtmpv.generators.css;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import joist.sourcegen.GClass;
import joist.sourcegen.GMethod;

import org.gwtmpv.generators.Cleanup;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.resources.client.CssResource;

/** A utility class for creating a Java interface declaration for a given CSS file. */
public class CssGenerator extends AbstractCssGenerator {

  private final File inputCssFile;
  private final GClass cssInterface;

  public CssGenerator(final File inputCssFile, Cleanup cleanup, final String interfaceName, final File outputDirectory) {
    super(outputDirectory, cleanup);
    this.inputCssFile = inputCssFile;
    cssInterface = new GClass(interfaceName).setInterface().baseClass(CssResource.class);
  }

  public void run() throws IOException {
    addMethods();
    markAndSaveIfChanged(cssInterface);
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
