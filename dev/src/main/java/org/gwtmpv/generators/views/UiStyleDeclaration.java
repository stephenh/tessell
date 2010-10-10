package org.gwtmpv.generators.views;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/** A DTO for {@code ui:style} declarations. */
class UiStyleDeclaration {

  final String type;
  final String name;
  String css = "";

  UiStyleDeclaration(final String type, final String name) {
    this.type = type;
    this.name = name;
  }

  /** @return the css content from our ui:style snippet in a tmp file */
  File getCssInFile() {
    try {
      File f = File.createTempFile(name, ".tmp");
      FileUtils.writeStringToFile(f, css);
      f.deleteOnExit();
      return f;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
