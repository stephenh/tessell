package org.gwtmpv.widgets;

import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

public interface IsWidget extends EventListener, HasHandlers, HasCss {
  // HasCss addStyleName/removeStyleName is really from IsUIObject

  // really from IsUIObject
  int getAbsoluteTop();

  // really from IsUIObject
  int getAbsoluteLeft();

  // really from IsUIObject
  int getOffsetHeight();

  // really from IsUIObject
  int getOffsetWidth();

  void ensureDebugId(String id);

  IsElement getIsElement();

  Widget asWidget();

}
