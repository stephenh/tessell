package org.tessell.model.dsl;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ChangeBinder extends EventBinder {

  private final HasValueChangeHandlers<Object> changable;

  ChangeBinder(HasValueChangeHandlers<Object> changable) {
    this.changable = changable;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return changable.addValueChangeHandler(new ValueChangeHandler<Object>() {
      public void onValueChange(ValueChangeEvent<Object> event) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return changable.addValueChangeHandler(new ValueChangeHandler<Object>() {
      public void onValueChange(ValueChangeEvent<Object> event) {
        runnable.run(null);
      }
    });
  }
}
