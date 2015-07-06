package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.AbstractModel;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.StringProperty;

public class LambdaModelTest {

  public class SomeDto {
    public String name;
    public boolean enabled;

    public SomeDto(String name, boolean enabled) {
      this.name = name;
      this.enabled = enabled;
    }
  }

  public class SomeModel extends AbstractModel {

    public final StringProperty name;
    public final BooleanProperty enabled;

    public SomeModel(SomeDto dto) {
      name = add(stringProperty("name", () -> dto.name, v -> dto.name = v));
      enabled = add(booleanProperty("enabled", () -> dto.enabled, v -> dto.enabled = v));
    }
  }

  @Test
  public void lambdas() {
    SomeDto dto = new SomeDto("foo", false);
    SomeModel m = new SomeModel(dto);
    assertThat(m.name.get(), is("foo"));
    m.name.set("bar");
    assertThat(dto.name, is("bar"));
  }
}
