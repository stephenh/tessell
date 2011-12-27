package org.tessell.generators.views;

import static org.apache.commons.lang.StringUtils.substringAfterLast;
import static org.apache.commons.lang.StringUtils.substringBeforeLast;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/** A DTO for {@code ui:style} declarations. */
class UiStyleDeclaration {

  final String type;
  final String name;
  String css = "";
  private File cssFile;

  UiStyleDeclaration(final String type, final String name) {
    this.type = type;
    this.name = name;
  }

  /** @return the css content from our ui:style snippet in a tmp file */
  File getCssInFile() {
    if (cssFile == null) {
      try {
        cssFile = File.createTempFile(name, ".tmp");
        cssFile.deleteOnExit();
        FileUtils.writeStringToFile(cssFile, css);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return cssFile;
  }

  public String getStubClassName() {
    final String packageName = substringBeforeLast(type, ".");
    final String simpleName = substringAfterLast(type, ".");
    return packageName + ".Stub" + simpleName;
  }

}
