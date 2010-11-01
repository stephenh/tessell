package org.gwtmpv.widgets.cellview;

public abstract class SimpleValue<C> {

  public abstract C get();

  public void set(final C value) {
    throw new UnsupportedOperationException(this + " is read only");
  }

}
