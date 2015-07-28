package org.tessell.tests.model.properties;

import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.tessell.model.AbstractDtoModel;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.StringProperty;

public class SomeModel extends AbstractDtoModel<SomeDto> {

  public final StringProperty name = add(stringProperty("name", () -> dto.name, v -> dto.name = v));
  public final BooleanProperty enabled = add(booleanProperty("enabled", () -> dto.enabled, v -> dto.enabled = v));

  public SomeModel(SomeDto dto) {
    super(dto);
    merge(dto);
  }
}
