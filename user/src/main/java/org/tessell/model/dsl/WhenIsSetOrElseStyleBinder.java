package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

/** Sets the style based on the property value. */
public class WhenIsSetOrElseStyleBinder<P> {

  private final Binder b;
  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final String ifTrue;
  private final String ifFalse;

  public WhenIsSetOrElseStyleBinder(Binder b, Property<P> property, WhenCondition<P> condition, String ifTrue, String ifFalse) {
    this.b = b;
    this.property = property;
    this.condition = condition;
    this.ifTrue = ifTrue;
    this.ifFalse = ifFalse;
  }

  /** Sets/removes our {@code style} when our property is {@code true}. */
  public void on(final HasCss css) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(css);
      }
    }));
    update(css); // set initial value
  }

  private void update(HasCss css) {
    if (condition.evaluate(property)) {
      css.addStyleName(ifTrue);
      css.removeStyleName(ifFalse);
    } else {
      css.removeStyleName(ifTrue);
      css.addStyleName(ifFalse);
    }
  }
}
