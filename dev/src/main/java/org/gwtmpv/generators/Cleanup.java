package org.gwtmpv.generators;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import joist.sourcegen.GClass;

import org.apache.commons.io.FileUtils;

public class Cleanup {

  private final File outputDirectory;
  private final List<String> filesAssumedBad = new ArrayList<String>();

  public Cleanup(final File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  @SuppressWarnings("unchecked")
  public void watchPackage(String packageName) {
    File packageDirectory = new File(outputDirectory, packageName.replace(".", "/"));
    for (File file : (List<File>) FileUtils.listFiles(packageDirectory, null, true)) {
      filesAssumedBad.add(file.getAbsolutePath());
    }
  }

  public void markOkay(File file) {
    filesAssumedBad.remove(file.getAbsolutePath());
  }

  public void markOkay(GClass gc) {
    filesAssumedBad.remove(new File(outputDirectory, gc.getFileName()).getAbsolutePath());
  }

  public void deleteLeftOvers() {
    for (String bad : filesAssumedBad) {
      System.out.println("deleting " + bad);
      new File(bad).delete();
    }
  }

}
