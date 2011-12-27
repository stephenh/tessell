package org.tessell.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.tessell.model.properties.NewProperty.stringProperty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.StringProperty;
import org.tessell.util.PropertyUtils;

public class PropertyUtilsTest {

  @Test
  public void defaultValue() {
    StringProperty original = stringProperty("p");
    Property<String> derived = PropertyUtils.defaultValue(original, "isnull");
    assertThat(derived.get(), is("isnull"));
    original.set("somevalue");
    assertThat(derived.get(), is("somevalue"));
  }

  @Test
  public void defaultValueFiresEvents() {
    StringProperty original = stringProperty("p");
    Property<String> derived = PropertyUtils.defaultValue(original, "isnull");

    ChangedWatcher w = new ChangedWatcher();
    derived.addPropertyChangedHandler(w);

    original.set("blah");
    original.set(null);
    original.set("fixed");
    assertThat(w.values, contains("blah", "isnull", "fixed"));
  }

  private static class ChangedWatcher implements PropertyChangedHandler<String> {
    private final List<String> values = new ArrayList<String>();

    @Override
    public void onPropertyChanged(PropertyChangedEvent<String> event) {
      values.add(event.getProperty().get());
    }
  }

}
