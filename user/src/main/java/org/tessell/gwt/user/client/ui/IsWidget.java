package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.IsElement;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.HasAttachHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.EventListener;

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

  IsWidget getIsParent();

  <H extends EventHandler> HandlerRegistration addDomHandler(final H handler, DomEvent.Type<H> type);

}
