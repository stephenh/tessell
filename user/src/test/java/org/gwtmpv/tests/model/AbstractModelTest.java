package org.gwtmpv.tests.model;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.gwtmpv.model.AbstractModel;
import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.model.validation.Valid;
import org.junit.Test;

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
