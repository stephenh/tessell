/*
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tessell.generators.css;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import joist.sourcegen.GClass;

import org.tessell.generators.Cleanup;
import org.tessell.generators.GenUtils;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.dev.util.log.PrintWriterTreeLogger;
import com.google.gwt.resources.css.ExtractClassNamesVisitor;
import com.google.gwt.resources.css.GenerateCssAst;
import com.google.gwt.resources.css.InterfaceGenerator;
import com.google.gwt.resources.css.ast.Context;
import com.google.gwt.resources.css.ast.CssDef;
import com.google.gwt.resources.css.ast.CssProperty.Value;
import com.google.gwt.resources.css.ast.CssStylesheet;
import com.google.gwt.resources.css.ast.CssVisitor;

/**
 * A utility class for creating a Java interface declaration for a given CSS file.
 *
 * Thanks to everything being private, this is a huge copy/paste from {@link InterfaceGenerator}.
 */
public class AbstractCssGenerator {

  // cache the PrintWriter only if we need it
  private static PrintWriter logWriter;
  private final File inputFile;
  private final File outputDirectory;
  private final Cleanup cleanup;
  private final Map<String, String> classToMethod = new TreeMap<String, String>(CSS_CLASS_COMPARATOR);
  private final Map<String, Value> defs = new TreeMap<String, Value>();

  protected AbstractCssGenerator(File inputFile, final File outputDirectory, final Cleanup cleanup) {
    this.inputFile = inputFile;
    this.outputDirectory = outputDirectory;
    this.cleanup = cleanup;
    lazyInitStaticLogWriter();
    CssStylesheet sheet = parseFile();
    extractClassNames(sheet);
    extractDefs(sheet);
  }

  /** @return a sorted map of class name -> method name for {@code inputFile} */
  protected Map<String, String> getClassNameToMethodName() {
    return classToMethod;
  }

  /** @return a sorted list of defs. */
  protected Map<String, Value> getDefs() {
    return defs;
  }

  private void lazyInitStaticLogWriter() {
    if (logWriter == null) {
      try {
        logWriter = new PrintWriter(new File(outputDirectory, ".cssGenerator.log"));
      } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  protected void markAndSaveIfChanged(GClass gc) {
    cleanup.markOkay(gc);
    GenUtils.saveIfChanged(outputDirectory, gc);
  }

  private void extractClassNames(CssStylesheet sheet) {
    // De-duplicate method names
    final Set<String> methodNames = new HashSet<String>();
    for (final String className : ExtractClassNamesVisitor.exec(sheet)) {
      String methodName = GenUtils.toMethodName(className);
      while (!methodNames.add(methodName)) {
        methodName += "_"; // Unusual, handles foo-bar and foo--bar
      }
      if (JavaKeywords.is(methodName)) {
        methodName += "_"; // change abstract to abstract_
      }
      classToMethod.put(className, methodName);
    }
  }

  private void extractDefs(CssStylesheet sheet) {
    new CssVisitor() {
      @Override
      public void endVisit(CssDef x, Context ctx) {
        if (x.getValues().size() > 0) {
          defs.put(x.getKey(), x.getValues().get(0));
        }
      }
    }.accept(sheet);
  }

  private CssStylesheet parseFile() {
    try {
      // Create AST
      PrintWriterTreeLogger logger = new PrintWriterTreeLogger(logWriter);
      return GenerateCssAst.exec(logger, inputFile.toURI().toURL());
    } catch (final MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (final UnableToCompleteException e) {
      throw new RuntimeException(e);
    } finally {
      logWriter.flush();
    }
  }

  private static final Comparator<String> CSS_CLASS_COMPARATOR = new Comparator<String>() {
    public int compare(final String o1, final String o2) {
      return o1.compareToIgnoreCase(o2);
    }
  };
}
