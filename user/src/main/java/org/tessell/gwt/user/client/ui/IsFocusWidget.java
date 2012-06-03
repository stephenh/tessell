package org.tessell.gwt.user.client.ui;

import org.tessell.widgets.IsWidget;

import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasEnabled;

public interface IsFocusWidget extends IsWidget, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers, HasAllMouseHandlers, HasEnabled {

  int getTabIndex();

  boolean isEnabled();

  void setAccessKey(char key);

  void setEnabled(boolean enabled);

  void setFocus(boolean focused);

  void setTabIndex(int index);
}
