package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.model.properties.Property;

/** Sets the style based on the property value. */
public class WhenIsSetStyleBinder<P> {

  private final Binder b;
  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final String style;

  public WhenIsSetStyleBinder(Binder b, Property<P> property, WhenCondition<P> condition, final String style) {
    this.b = b;
    this.property = property;
    this.style = style;
    this.condition = condition;
  }

  /** Sets/removes our {@code style} when our property is {@code true}. */
  public void on(final HasCss... css) {
    b.add(property.addPropertyChangedHandler(e -> update(css)));
    update(css); // set initial value
  }

  private void update(HasCss... csses) {
    if (condition.evaluate(property)) {
      for (HasCss css : csses) {
        css.addStyleName(style);
      }
    } else {
      for (HasCss css : csses) {
        css.removeStyleName(style);
      }
    }
  }
}
