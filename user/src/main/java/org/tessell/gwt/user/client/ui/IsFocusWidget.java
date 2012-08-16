package org.tessell.gwt.user.client.ui;

import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasFocus;

@SuppressWarnings("deprecation")
public interface IsFocusWidget extends IsWidget, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers, HasAllMouseHandlers, HasEnabled, HasFocus {

  boolean isEnabled();

  void setEnabled(boolean enabled);

}
