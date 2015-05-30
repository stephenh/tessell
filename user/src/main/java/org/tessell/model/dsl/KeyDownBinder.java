package org.tessell.model.dsl;

import java.util.List;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
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
    return keyDownable.addKeyDownHandler(e -> {
      if (charFilter == null || charFilter.contains(e.getNativeKeyCode())) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return keyDownable.addKeyDownHandler(e -> {
      if (charFilter == null || charFilter.contains(e.getNativeKeyCode())) {
        runnable.run(e);
      }
    });
  }

}
