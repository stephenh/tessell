package org.tessell.tests.model.properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.and;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.or;

import org.junit.Test;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.Property;
import org.tessell.tests.model.validation.rules.AbstractRuleTest;

public class BooleanPropertyTest extends AbstractRuleTest {

  @Test
  public void toggleFromNullToTrue() {
    BooleanProperty p = booleanProperty("p");
    p.toggle();
    assertThat(p.get(), is(TRUE));
  }

  @Test
  public void toggleFromFalseToTrue() {
    BooleanProperty p = booleanProperty("p", false);
    p.toggle();
    assertThat(p.get(), is(TRUE));
  }

  @Test
  public void toggleFromTrueToFalse() {
    BooleanProperty p = booleanProperty("p", true);
    p.toggle();
    assertThat(p.get(), is(FALSE));
  }

  @Test
  public void testNot() {
    BooleanProperty a = booleanProperty("a");
    Property<Boolean> b = a.not();
    assertThat(b.get(), is(nullValue()));

    a.set(true);
    assertThat(b.get(), is(false));

    a.set(false);
    assertThat(b.get(), is(true));

    b.set(false);
    assertThat(a.get(), is(true));

    b.set(true);
    assertThat(a.get(), is(false));

    b.set(null);
    assertThat(a.get(), is(nullValue()));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testOr() {
    BooleanProperty a = booleanProperty("a");
    BooleanProperty b = booleanProperty("b");
    BooleanProperty o = or(a, b);
    assertThat(o.get(), is(false));

    a.set(true);
    assertThat(o.get(), is(true));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testAnd() {
    BooleanProperty a = booleanProperty("a");
    BooleanProperty b = booleanProperty("b");
    BooleanProperty o = and(a, b);
    assertThat(o.get(), is(false));

    a.set(true);
    assertThat(o.get(), is(false));

    b.set(true);
    assertThat(o.get(), is(true));
  }

  @Test
  public void testAbusingTheValueConstructorStillWorks() {
    Property<Boolean> p = booleanProperty("p", false);

    Property<Boolean> p1 = booleanProperty(p).not();
    final int changes[] = { 0 };
    p1.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        changes[0] = changes[0] + 1;
      }
    });

    p.set(true);
    assertThat(changes[0], is(1));
    assertThat(p1.get(), is(false));

    p.set(false);
    assertThat(changes[0], is(2));
    assertThat(p1.get(), is(true));

    // p1 is technically writeable as it will call Value.set
    assertThat(p1.isReadOnly(), is(false));
    p1.set(false);
    assertThat(changes[0], is(3));
    assertThat(p.get(), is(true));
  }

}
