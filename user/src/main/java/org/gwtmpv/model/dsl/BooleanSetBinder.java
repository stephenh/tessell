package org.gwtmpv.model.dsl;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.HasCss;

/** Sets the style based on the property value. */
public class BooleanSetBinder {

  private final Binder binder;
  private final Property<Boolean> property;
  private final String style;

  public BooleanSetBinder(final Binder binder, Property<Boolean> property, final String style) {
    this.binder = binder;
    this.property = property;
    this.style = style;
  }

  /** Sets/removes our {@code style} when our property is {@code true}. */
  public void on(final HasCss css) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<Boolean>() {
      public void onPropertyChanged(PropertyChangedEvent<Boolean> event) {
        update(css);
      }
    }));
    update(css); // set initial value
  }

  private void update(HasCss css) {
    if (Boolean.TRUE.equals(property.get())) {
      css.addStyleName(style);
    } else {
      css.removeStyleName(style);
    }
  }
}
