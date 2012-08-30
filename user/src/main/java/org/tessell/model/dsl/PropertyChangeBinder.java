package org.tessell.model.dsl;

import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.properties.Property;

import com.google.gwt.event.shared.HandlerRegistration;

public class PropertyChangeBinder extends EventBinder {

  private final Property<Object> property;

  protected PropertyChangeBinder(final Binder b, final Property<Object> property) {
    super(b);
    this.property = property;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return property.addPropertyChangedHandler(new PropertyChangedHandler<Object>() {
      public void onPropertyChanged(PropertyChangedEvent<Object> event) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return property.addPropertyChangedHandler(new PropertyChangedHandler<Object>() {
      public void onPropertyChanged(PropertyChangedEvent<Object> event) {
        runnable.run(null);
      }
    });
  }
}
