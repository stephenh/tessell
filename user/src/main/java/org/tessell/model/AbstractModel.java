package org.tessell.model;

import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;

/** A base class for models. Provides a {@link PropertyGroup} for all of the properties. */
public abstract class AbstractModel implements Model {

  protected PropertyGroup all = new PropertyGroup("all", "model invalid");

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
