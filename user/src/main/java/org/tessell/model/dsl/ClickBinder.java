package org.tessell.model.dsl;

import org.tessell.gwt.user.client.ui.IsWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ClickBinder extends EventBinder {

  private final IsWidget clickable;

  ClickBinder(final Binder b, final IsWidget clickable) {
    super(b);
    this.clickable = clickable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return clickable.addDomHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        runnable.run();
      }
    }, ClickEvent.getType());
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return clickable.addDomHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        runnable.run(event);
      }
    }, ClickEvent.getType());
  }

}
