package org.tessell.generators;

import java.io.File;
import java.util.Map;

import org.tessell.generators.resources.ResourcesGenerator;
import org.tessell.generators.views.ViewGenerator;

import com.google.gwt.junit.GWTMockUtilities;

/**
 * Front-end for resources and views generators.
 *
 * Both run quickly enough now that we can fire them off together.
 */
public class Generator {

  private final File inputDirectory;
  private final File outputDirectory;
  private String viewsPackageName;
  private String resourcesPackageName;
  private String[] additionalViewgenFiles = new String[0];

  public Generator(File inputDirectory, File outputDirectory) {
    this.inputDirectory = inputDirectory;
    this.outputDirectory = outputDirectory;
  }

  public void setViewsPackageName(String viewsPackageName) {
    this.viewsPackageName = viewsPackageName;
  }

  public void setResourcesPackageName(String resourcesPackageName) {
    this.resourcesPackageName = resourcesPackageName;
  }

  public void setAdditionalViewgenFiles(String... additionalViewgenFiles) {
    this.additionalViewgenFiles = additionalViewgenFiles;
  }

  /**
   * Generates the resources and views.
   *
   * @throws Exception Thrown if any errors occur during generation.
   */
  public void generate() throws Exception {
    // We use reflection to get at annotations of our widget subclasses,
    // but that wanders into UIObject, which has a static clinit call
    // to UIObject. This makes it return null instead of blowing up.
    GWTMockUtilities.disarm();

    long start = System.currentTimeMillis();

    final Cleanup cleanup = new Cleanup(outputDirectory);

    if (viewsPackageName != null) {
      cleanup.watchPackage(viewsPackageName);
      new ViewGenerator(inputDirectory, viewsPackageName, outputDirectory, cleanup, additionalViewgenFiles).generate();
    }

    if (resourcesPackageName != null) {
      cleanup.watchPackage(resourcesPackageName);
      new ResourcesGenerator(inputDirectory, cleanup, resourcesPackageName, outputDirectory).run();
    }

    cleanup.deleteLeftOvers();

    long end = System.currentTimeMillis();
    System.out.println("Done " + (end - start) + "ms");
  }

  /** Args: {@code --inputDirectory src/main/java --viewsPackageName com.app.views --resourcesPackageName com.app.resources --outputDirectory target/gen}. */
  public static void main(final String[] args) throws Exception {
    final Map<String, String> settings = GenUtils.parseArgs(args);
    final File inputDirectory = new File(settings.get("inputDirectory"));
    final File outputDirectory = new File(settings.get("outputDirectory"));
    final String viewsPackageName = settings.get("viewsPackageName");
    final String resourcesPackageName = settings.get("resourcesPackageName");

    Generator generator = new Generator(inputDirectory, outputDirectory);
    generator.setViewsPackageName(viewsPackageName);
    generator.setResourcesPackageName(resourcesPackageName);
    generator.generate();
  }
}
