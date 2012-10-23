package org.tessell.widgets;

/** Base class for generated views to extend. */
public abstract class StubView extends CompositeIsWidget {

  @Override
  public void ensureDebugId(final String id) {
    onEnsureDebugId(id);
    super.ensureDebugId(id);
  }

  // for subclasses to override
  protected void onEnsureDebugId(String ensureDebugId) {
  }

}
