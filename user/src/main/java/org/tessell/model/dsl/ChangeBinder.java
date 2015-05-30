package org.tessell.model.dsl;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChangeBinder extends EventBinder {

  private final HasValueChangeHandlers<Object> changable;

  ChangeBinder(final Binder b, final HasValueChangeHandlers<Object> changable) {
    super(b);
    this.changable = changable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return changable.addValueChangeHandler(e -> runnable.run());
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return changable.addValueChangeHandler(e -> runnable.run(null));
  }
}
