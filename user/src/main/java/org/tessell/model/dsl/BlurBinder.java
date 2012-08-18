package org.tessell.model.dsl;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class BlurBinder extends EventBinder {

  private final HasBlurHandlers blurable;

  BlurBinder(HasBlurHandlers blurable) {
    this.blurable = blurable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return blurable.addBlurHandler(new BlurHandler() {
      public void onBlur(BlurEvent event) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return blurable.addBlurHandler(new BlurHandler() {
      public void onBlur(BlurEvent event) {
        runnable.run(event);
      }
    });
  }

}
