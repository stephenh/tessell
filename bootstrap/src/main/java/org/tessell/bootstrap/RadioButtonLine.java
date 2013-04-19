package org.tessell.bootstrap;

import org.tessell.model.properties.Property;

import com.google.gwt.uibinder.client.UiConstructor;

/** A form line for properties that have selections of radio buttons. */
public class RadioButtonLine extends BasicLine {

  private final String name;

  @UiConstructor
  public RadioButtonLine(final String name) {
    this.name = name;
    alwaysHideOptionalLabel();
  }

  public <P> MoreValues<P> bind(final Property<P> property) {
    addToValid(property);
    return new MoreValues<P>(property);
  }

  /** Called each time the fluent MoreValues gets a new value, so that StubRadioButtonLine can hook in. */
  protected <P> void valueAdded(final Property<P> property, final P value, final String label) {
    final RadioButton button = new RadioButton(name);
    button.setText(label);
    disableWhenActive(button);
    addContent(button);
    b.bind(property).to(button, value);
  }

  /** Fluent method to chain "addValue" calls. */
  public class MoreValues<P> {
    private final Property<P> property;

    public MoreValues(final Property<P> property) {
      this.property = property;
    }

    public MoreValues<P> addValue(final P value, final String label) {
      valueAdded(property, value, label);
      return this;
    }
  }

}
