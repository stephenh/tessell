package org.tessell.widgets;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

public interface IsWidget extends EventListener, HasHandlers, HasCss, HasAttachHandlers, com.google.gwt.user.client.ui.IsWidget {

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

  boolean isAttached();

  IsElement getIsElement();

  Widget asWidget();

}
