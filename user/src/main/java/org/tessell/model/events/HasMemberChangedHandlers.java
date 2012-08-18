package org.tessell.model.events;

import com.google.gwt.event.shared.HandlerRegistration;

public interface HasMemberChangedHandlers {

  HandlerRegistration addMemberChangedHandler(MemberChangedHandler handler);

}
