package org.tessell.model.dsl;

import org.tessell.model.properties.Property;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class ClickBinder {

  private final HasClickHandlers clickable;

  ClickBinder(HasClickHandlers clickable) {
    this.clickable = clickable;
  }

  public <P> SetPropertyBinder<P> set(Property<P> property) {
    return new SetPropertyBinder<P>(property, new SetPropertyBinder.Setup() {
      public HandlerRegistrations setup(final Runnable runnable) {
        return new HandlerRegistrations(clickable.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent arg0) {
            runnable.run();
          }
        }));
      }
    });
  }

}
