package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.Model;
import org.gwtmpv.model.properties.StringProperty;

public class DummyModel implements Model<DummyModel> {

  public final StringProperty name = stringProperty("name").max(50);

  @Override
  public void merge(final DummyModel dto) {
  }

}
