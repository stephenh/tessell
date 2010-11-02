package org.gwtmpv.widgets.cellview;

/** A header value. */
public abstract class HeaderValue<C> {

  public abstract C get();

  public void set(final C value) {
    // default to nothing
  }

}
