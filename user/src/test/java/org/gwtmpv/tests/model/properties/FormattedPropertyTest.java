package org.gwtmpv.tests.model.properties;

import static org.gwtmpv.model.properties.NewProperty.integerProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedEvent.PropertyChangedHandler;
import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyFormatter;
import org.junit.Test;

public class FormattedPropertyTest {

  @Test
  public void get() {
    Property<String> p = integerProperty("i", 1).formatted(f);
    assertThat(p.get(), is("1-1"));
  }

  @Test
  public void onChangeFires() {
    IntegerProperty i = integerProperty("i", 1);
    Property<String> p = i.formatted(f);

    ChangeTracker tracker = new ChangeTracker();
    p.addPropertyChangedHandler(tracker);

    i.set(2);
    assertThat(tracker.values, contains("2-2"));
  }

  private final PropertyFormatter<Integer, String> f = new PropertyFormatter<Integer, String>() {
    @Override
    public String format(Integer a) {
      return a + "-" + a;
    }
  };

  private class ChangeTracker implements PropertyChangedHandler<String> {
    private final List<String> values = new ArrayList<String>();

    @Override
    public void onPropertyChanged(PropertyChangedEvent<String> event) {
      values.add(event.getProperty().get());
    }
  }

}
