package org.tessell.tests.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.AbstractModel;
import org.tessell.model.events.MemberChangedEvent;
import org.tessell.model.events.MemberChangedHandler;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.properties.NewProperty;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.Valid;
import org.tessell.model.validation.events.RuleTriggeredEvent;
import org.tessell.model.validation.events.RuleTriggeredHandler;

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

  @Test
  public void allFiresAnErrorMessage() {
    final String[] message = { null };
    e.allValid().addRuleTriggeredHandler(new RuleTriggeredHandler() {
      public void onTrigger(RuleTriggeredEvent event) {
        message[0] = event.getMessage();
      }
    });
    e.name.setTouched(true);
    assertThat(message[0], is("model invalid"));
  }

  @Test
  public void firesMemberChangedEvent() {
    final int[] fires = { 0 };
    EmployeeModel m = new EmployeeModel();
    m.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        fires[0]++;
      }
    });
    m.name.set("asdf");
    assertThat(fires[0], is(1));
    m.address.set("asdf");
    assertThat(fires[0], is(2));
  }

  @Test
  public void firesMemberChangedEventForListProperties() {
    final int[] fires = { 0 };
    EmployeeModel m = new EmployeeModel();
    m.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        fires[0]++;
      }
    });
    AccountModel a = new AccountModel();
    m.accounts.add(a);
    assertThat(fires[0], is(1));
    a.name.set("asdf");
    assertThat(fires[0], is(2));
  }

  public static class EmployeeModel extends AbstractModel {
    public final IntegerProperty id = add(integerProperty("id").req());
    public final StringProperty name = add(stringProperty("name").req());
    public final StringProperty address = add(stringProperty("address"));
    public final ListProperty<AccountModel> accounts = add(NewProperty.<AccountModel> listProperty("accounts"));
  }

  public static class AccountModel extends AbstractModel {
    public final StringProperty name = add(stringProperty("name"));
  }

}
