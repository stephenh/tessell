package org.tessell.widgets;

import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.ui.HasCss;

import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Augments GWT's {@code IsWidget} with more methods, and unifies the characteristic
 * {@code HasXxx} interfaces, so that presenters can unit test against this interface.
 */
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
