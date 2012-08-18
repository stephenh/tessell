package org.tessell.model.dsl;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class DoubleClickBinder extends EventBinder {

  private final HasDoubleClickHandlers clickable;

  DoubleClickBinder(HasDoubleClickHandlers clickable) {
    this.clickable = clickable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return clickable.addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return clickable.addDoubleClickHandler(new DoubleClickHandler() {
      public void onDoubleClick(DoubleClickEvent event) {
        runnable.run(event);
      }
    });
  }

}
