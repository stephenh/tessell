package org.tessell.model.dsl;

import java.util.List;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class KeyDownBinder extends EventBinder {

  private final HasKeyDownHandlers keyDownable;
  private final List<Integer> charFilter;

  public KeyDownBinder(final Binder b, final HasKeyDownHandlers keyDownable, final List<Integer> charFilter) {
    super(b);
    this.keyDownable = keyDownable;
    this.charFilter = charFilter;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return keyDownable.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (charFilter == null || charFilter.contains(event.getNativeKeyCode())) {
          runnable.run();
        }
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return keyDownable.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (charFilter == null || charFilter.contains(event.getNativeKeyCode())) {
          runnable.run(event);
        }
      }
    });
  }

}
