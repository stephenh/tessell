package org.tessell.widgets;

/**
 * Interface for setting just the suffix of the elements
 * in a ui.xml file.
 *
 * E.g. ensureDebugIdSuffix("foo") will result in ids of
 * gwt-debug-SomeFile-someElement-foo.
 */
public interface HasEnsureDebugIdSuffix {

  void ensureDebugIdSuffix(String suffix);

}
