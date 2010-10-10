package org.gwtmpv.generators;

import java.io.File;
import java.util.Map;

import org.gwtmpv.generators.resources.ResourcesGenerator;
import org.gwtmpv.generators.views.ViewGenerator;

/**
 * Front-end for resources and views generators.
 *
 * Both run quickly enough now that we can fire them off together.
 */
public class Generator {

  /** Args: {@code --inputDirectory src/main/java --viewsPackage com.app.views --resourcesPackage com.app.resources --outputDirectory target/gen}. */
  public static void main(final String[] args) throws Exception {
    final Map<String, String> settings = GenUtils.parseArgs(args);
    final File input = new File(settings.get("inputDirectory"));
    final File output = new File(settings.get("outputDirectory"));

    final String viewsPackage = settings.get("viewsPackageName");
    if (viewsPackage != null) {
      new ViewGenerator(input, viewsPackage, output).generate();
    }

    final String resourcesPackage = settings.get("resourcesPackageName");
    if (resourcesPackage != null) {
      new ResourcesGenerator(input, resourcesPackage, output).run();
    }
  }

}
