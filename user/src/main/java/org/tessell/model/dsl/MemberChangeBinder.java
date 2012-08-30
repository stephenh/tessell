package org.tessell.model.dsl;

import org.tessell.model.events.HasMemberChangedHandlers;
import org.tessell.model.events.MemberChangedEvent;
import org.tessell.model.events.MemberChangedHandler;

import com.google.gwt.event.shared.HandlerRegistration;

public class MemberChangeBinder extends EventBinder {

  private final HasMemberChangedHandlers hasMembers;

  protected MemberChangeBinder(final Binder b, final HasMemberChangedHandlers hasMembers) {
    super(b);
    this.hasMembers = hasMembers;
  }

  @Override
  protected HandlerRegistration hookUpRunnable(final Runnable runnable) {
    return hasMembers.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        runnable.run();
      }
    });
  }

  @Override
  protected HandlerRegistration hookUpEventRunnable(final DomEventRunnable runnable) {
    return hasMembers.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        runnable.run(null); // we're not technically a dom event...
      }
    });
  }

}
