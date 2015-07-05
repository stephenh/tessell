package org.tessell.gwt.user.client.ui;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasFocus;

@SuppressWarnings("deprecation")
public interface IsFocusWidget extends IsWidget, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers, HasAllMouseHandlers, HasEnabled, HasFocus,
HasAllDragAndDropHandlers {

  boolean isEnabled();

  void setEnabled(boolean enabled);

}
