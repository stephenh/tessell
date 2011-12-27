package org.tessell.tests.model.properties;

import static org.tessell.model.properties.NewProperty.stringProperty;

import org.tessell.model.AbstractModel;
import org.tessell.model.properties.StringProperty;

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
