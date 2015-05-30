package org.tessell.model.dsl;

import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class BlurBinder extends EventBinder {

  private final HasBlurHandlers blurable;

  BlurBinder(final Binder b, final HasBlurHandlers blurable) {
    super(b);
    this.blurable = blurable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return blurable.addBlurHandler(e -> runnable.run());
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return blurable.addBlurHandler(e -> runnable.run(e));
  }

}
