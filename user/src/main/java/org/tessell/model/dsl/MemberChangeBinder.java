package org.tessell.model.dsl;

import org.tessell.model.events.HasMemberChangedHandlers;

import com.google.gwt.event.shared.HandlerRegistration;

public class MemberChangeBinder extends EventBinder {

  private final HasMemberChangedHandlers hasMembers;

  protected MemberChangeBinder(final Binder b, final HasMemberChangedHandlers hasMembers) {
    super(b);
    this.hasMembers = hasMembers;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return hasMembers.addMemberChangedHandler(e -> runnable.run());
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    // we're not technically a dom event, so pass null
    return hasMembers.addMemberChangedHandler(e -> runnable.run(null));
  }

}
