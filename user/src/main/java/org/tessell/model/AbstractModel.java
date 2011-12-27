package org.tessell.model;

import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;

public abstract class AbstractModel<T> implements Model<T> {

  protected PropertyGroup all = new PropertyGroup("all", null);

  public PropertyGroup all() {
    return all;
  }

  @Override
  public Property<Boolean> allValid() {
    return all;
  }

  /** Adds {@code p} to the property group. */
  protected <P extends Property<?>> P add(P p) {
    all.add(p);
    return p;
  }

}
