package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

/** Sets the style based on the property value. */
public class WhenIsSetBinder<P> {

  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final String style;

  public WhenIsSetBinder(Property<P> property, WhenCondition<P> condition, final String style) {
    this.property = property;
    this.style = style;
    this.condition = condition;
  }

  /** Sets/removes our {@code style} when our property is {@code true}. */
  public HandlerRegistrations on(final HasCss css) {
    HandlerRegistrations hr = new HandlerRegistrations();
    hr.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(css);
      }
    }));
    update(css); // set initial value
    return hr;
  }

  private void update(HasCss css) {
    if (condition.evaluate(property)) {
      css.addStyleName(style);
    } else {
      css.removeStyleName(style);
    }
  }
}
