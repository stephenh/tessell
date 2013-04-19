package org.tessell.bootstrap;

import static java.lang.Boolean.TRUE;

import java.util.HashMap;
import java.util.Map;

import org.tessell.model.properties.Property;

public class StubRadioButtonLine extends RadioButtonLine {

  private final Map<Object, Property<?>> valueToProperty = new HashMap<Object, Property<?>>();
  private final Map<Object, RadioButton> valueToButton = new HashMap<Object, RadioButton>();

  public StubRadioButtonLine() {
    super("name"); // tessell can't pull this out of the ui.xml yet
  }

  // Currently can't be type-safe because we don't have widget-level generics
  @SuppressWarnings("unchecked")
  public void select(final Object value) {
    final Property<?> p = valueToProperty.get(value);
    if (p == null) {
      throw new IllegalArgumentException("No property found for " + value);
    }
    ((Property<Object>) p).set(value);
  }

  public boolean isSelected(final Object value) {
    final RadioButton b = valueToButton.get(value);
    if (b == null) {
      throw new IllegalArgumentException("No button found for " + value);
    }
    return TRUE.equals(b.getValue());
  }

  @Override
  protected <P> void valueAdded(final Property<P> property, final P value, final String label) {
    super.valueAdded(property, value, label);
    valueToProperty.put(value, property);
    valueToButton.put(value, (RadioButton) view.placeholder().getIsWidget(view.placeholder().getWidgetCount() - 1));
  }
}
