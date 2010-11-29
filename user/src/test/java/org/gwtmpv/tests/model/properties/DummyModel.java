package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.stringProperty;

import org.gwtmpv.model.AbstractModel;
import org.gwtmpv.model.properties.StringProperty;

public class DummyModel extends AbstractModel<DummyModel> {

  public final StringProperty name = stringProperty("name").max(50);

  @Override
  public void merge(final DummyModel dto) {
  }

  @Override
  public DummyModel getDto() {
    return null;
  }

}
