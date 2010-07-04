package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;

public interface IsFocusWidget extends IsWidget, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers,
    HasAllMouseHandlers {

  int getTabIndex();

  boolean isEnabled();

  void setAccessKey(char key);

  void setEnabled(boolean enabled);

  void setFocus(boolean focused);

  void setTabIndex(int index);
}
