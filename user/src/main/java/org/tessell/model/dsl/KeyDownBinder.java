package org.tessell.model.dsl;

import java.util.List;

import org.tessell.gwt.user.client.ui.IsWidget;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class KeyDownBinder extends EventBinder {

  private final IsWidget keyDownable;
  private final List<Integer> charFilter;

  public KeyDownBinder(final Binder b, final IsWidget keyDownable, final List<Integer> charFilter) {
    super(b);
    this.keyDownable = keyDownable;
    this.charFilter = charFilter;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return keyDownable.addDomHandler(e -> {
      if (charFilter == null || charFilter.contains(e.getNativeKeyCode())) {
        runnable.run();
      }
    } , KeyDownEvent.getType());
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return keyDownable.addDomHandler(e -> {
      if (charFilter == null || charFilter.contains(e.getNativeKeyCode())) {
        runnable.run(e);
      }
    } , KeyDownEvent.getType());
  }

}
