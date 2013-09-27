package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.IsPanel;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

/** Adds {@code widget} to a panel based on the {@code value} of a {@code property}. */
public class WhenIsAttachBinder<P> {

  private final Binder b;
  private final Property<P> property;
  private final WhenCondition<P> condition;
  private final IsWidget widget;

  public WhenIsAttachBinder(Binder b, Property<P> property, WhenCondition<P> condition, IsWidget widget) {
    this.b = b;
    this.property = property;
    this.condition = condition;
    this.widget = widget;
  }

  /** Adds/removes our {@code widget} to/from {@code panel} our {@code property} is {@code value}. */
  public void to(final IsPanel panel) {
    b.add(property.addPropertyChangedHandler(new PropertyChangedHandler<P>() {
      public void onPropertyChanged(PropertyChangedEvent<P> event) {
        update(panel);
      }
    }));
    update(panel); // set initial value
  }

  private void update(IsPanel panel) {
    if (condition.evaluate(property)) {
      panel.add(widget);
    } else {
      panel.remove(widget);
    }
  }

}
