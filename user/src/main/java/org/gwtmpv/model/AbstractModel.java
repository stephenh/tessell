package org.gwtmpv.model;

import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyGroup;

public abstract class AbstractModel<T> implements Model<T> {

  protected PropertyGroup all = new PropertyGroup("all", null);

  @Override
  public Property<Boolean> allValid() {
    return all;
  }

}
