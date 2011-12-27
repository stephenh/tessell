package org.tessell.tests.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.AbstractModel;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.Valid;

public class AbstractModelTest {

  final EmployeeModel e = new EmployeeModel();

  @Test
  public void allValidTouchesEachProperty() {
    e.allValid().touch();
    assertThat(e.id.isTouched(), is(true));
    assertThat(e.name.isTouched(), is(true));
  }

  @Test
  public void allValidIsFalseIfPropertiesAreInvalid() {
    e.allValid().touch();
    assertThat(e.allValid().wasValid(), is(Valid.NO));
  }

  @Test
  public void allValidIsValidIfAllPropertiesAreValid() {
    e.id.set(1);
    e.name.set("f");
    e.allValid().touch();
    assertThat(e.allValid().wasValid(), is(Valid.YES));
  }

  @Test
  public void flipValidity() {
    e.id.set(1);
    e.name.set("f");
    e.allValid().touch();
    assertThat(e.allValid().wasValid(), is(Valid.YES));

    e.name.set("");
    assertThat(e.allValid().wasValid(), is(Valid.NO));

    e.name.set("a");
    assertThat(e.allValid().wasValid(), is(Valid.YES));
  }

  public static class EmployeeModel extends AbstractModel<EmployeeDto> {
    public final IntegerProperty id = integerProperty("id").req().in(all);
    public final StringProperty name = stringProperty("name").req().in(all);
    public final StringProperty address = stringProperty("address").in(all);

    @Override
    public void merge(EmployeeDto dto) {
    }

    @Override
    public EmployeeDto getDto() {
      return null;
    }
  }

  public static class EmployeeDto {
  }

}
