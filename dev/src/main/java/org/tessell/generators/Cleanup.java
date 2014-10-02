package org.tessell.generators;

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import joist.sourcegen.GClass;

import org.apache.commons.io.FileUtils;

public class Cleanup {

  private final File outputDirectory;
  private final Set<String> filesAssumedBad = new TreeSet<String>();

  public Cleanup(final File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  public void watchPackage(String packageName) {
    File packageDirectory = new File(outputDirectory, packageName.replace(".", File.separator));
    if (packageDirectory.exists()) {
      for (File file : FileUtils.listFiles(packageDirectory, null, true)) {
        filesAssumedBad.add(file.getAbsolutePath());
      }
    }
  }

  public void markTypeOkay(String className) {
    filesAssumedBad.remove(new File(outputDirectory, className.replace(".", File.separator) + ".java").getAbsolutePath());
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
