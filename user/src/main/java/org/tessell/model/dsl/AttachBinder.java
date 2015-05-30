package org.tessell.model.dsl;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.HandlerRegistration;

public class AttachBinder extends EventBinder {

  private final HasAttachHandlers attachable;
  private final boolean onAttach;

  AttachBinder(final Binder b, final HasAttachHandlers attachable, final boolean onAttach) {
    super(b);
    this.attachable = attachable;
    this.onAttach = onAttach;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return attachable.addAttachHandler(e -> {
      if (e.isAttached() == onAttach) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return attachable.addAttachHandler(e -> {
      if (e.isAttached() == onAttach) {
        runnable.run(null); // AttachEvent is a GwtEvent, not DomEvent
    }
  });
  }

}
