package org.tessell.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.tessell.model.properties.NewProperty.stringProperty;

import org.junit.Test;
import org.tessell.model.properties.DerivedProperty;
import org.tessell.model.properties.StringProperty;

public class DerivedPropertyTest {

  private final StringProperty s1 = stringProperty("s1", "a");
  private final StringProperty s2 = stringProperty("s2", "b");

  @Test
  public void testDerivedProperty() {
    DerivedProperty<String> d = new DerivedProperty<String>() {
      protected String getDerivedValue() {
        return s1.get() + s2.get();
      }
    };
    assertThat(d.get(), is("ab"));
  }

  @Test
  public void testListForChanges() {
    DerivedProperty<String> d = new DerivedProperty<String>() {
      protected String getDerivedValue() {
        return s1.get() + s2.get();
      }
    };
    CountChanges c = CountChanges.on(d);

    s1.set("c");

    assertThat(c.changes, is(1));
    assertThat(d.get(), is("cb"));
  }
}
