package org.tessell.widgets;

import java.util.ArrayList;
import java.util.List;

/** Base class for generated views to extend. */
public abstract class StubView extends StubWidget {

  // We make no attempt to reconstruct a faithful tree of the widget
  // relationships within the ui.xml file--we just flatten everything
  // down to a single list. This means widgets that might be parent/children
  // are instead siblings, but for the current purposes, e.g. "find this
  // id within the stub widgets", the hierachy being slightly malformed
  // is okay.
  protected final List<IsWidget> widgets = new ArrayList<IsWidget>();

  @Override
  protected IsWidget findInChildren(String id) {
    return findInChildren(widgets.iterator(), id);
  }

}
