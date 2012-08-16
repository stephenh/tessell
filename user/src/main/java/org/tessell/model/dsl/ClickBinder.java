package org.tessell.model.dsl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ClickBinder extends AbstractEventBinder {

  private final HasClickHandlers clickable;

  ClickBinder(HasClickHandlers clickable) {
    this.clickable = clickable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return clickable.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return clickable.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        runnable.run(event);
      }
    });
  }

}
