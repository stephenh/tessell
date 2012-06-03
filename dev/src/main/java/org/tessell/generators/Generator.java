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

  /** Args: {@code --inputDirectory src/main/java --viewsPackage com.app.views --resourcesPackage com.app.resources --outputDirectory target/gen}. */
  public static void main(final String[] args) throws Exception {
    // We use reflection to get at annotations of our widget subclasses,
    // but that wanders into UIObject, which has a static clinit call
    // to UIObject. This makes it return null instead of blowing up.
    GWTMockUtilities.disarm();

    long start = System.currentTimeMillis();

    final Map<String, String> settings = GenUtils.parseArgs(args);
    final File input = new File(settings.get("inputDirectory"));
    final File output = new File(settings.get("outputDirectory"));

    final Cleanup cleanup = new Cleanup(output);

    final String viewsPackage = settings.get("viewsPackageName");
    if (viewsPackage != null) {
      cleanup.watchPackage(viewsPackage);
      new ViewGenerator(input, viewsPackage, output, cleanup).generate();
    }

    final String resourcesPackage = settings.get("resourcesPackageName");
    if (resourcesPackage != null) {
      cleanup.watchPackage(resourcesPackage);
      new ResourcesGenerator(input, cleanup, resourcesPackage, output).run();
    }

    cleanup.deleteLeftOvers();

    long end = System.currentTimeMillis();
    System.out.println("Done " + (end - start) + "ms");
  }

}
