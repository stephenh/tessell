package org.gwtmpv.model.dsl;

import static org.gwtmpv.util.ObjectUtils.eq;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.widgets.HasCss;

/** Sets the style based on the property value. */
public class WhenIsSetBinder<P> {

  private final Binder binder;
  private final Property<P> property;
  private final P value;
  private final String style;

  public WhenIsSetBinder(final Binder binder, Property<P> property, P value, final String style) {
    this.binder = binder;
    this.property = property;
    this.value = value;
    this.style = style;
  }

  /** Sets/removes our {@code style} when our property is {@code true}. */
  public void on(final HasCss css) {
    binder.registerHandler(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(css);
      }
    }));
    update(css); // set initial value
  }

  private void update(HasCss css) {
    if (eq(value, property.get())) {
      css.addStyleName(style);
    } else {
      css.removeStyleName(style);
    }
  }
}
