package org.tessell.widgets.cellview;

/** Base class for users to provide a {@link #get(Object)} and optionally {@link #set(Object, Object)} implementation. */
public abstract class ColumnValue<T, C> {

  public abstract C get(T object);

  public void set(final T object, final C value) {
    // default to nothing
  }

}
