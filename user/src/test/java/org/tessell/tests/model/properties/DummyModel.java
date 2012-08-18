package org.tessell.tests.model.properties;

import static org.tessell.model.properties.NewProperty.stringProperty;

import org.tessell.model.AbstractModel;
import org.tessell.model.properties.StringProperty;

public class DummyModel extends AbstractModel {

  public final StringProperty name = add(stringProperty("name").max(50));

  public DummyModel() {
  }

  public DummyModel(String name) {
    this.name.set(name);
  }
}
