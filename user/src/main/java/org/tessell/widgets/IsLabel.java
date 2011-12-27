package org.tessell.widgets;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWordWrap;

public interface IsLabel extends IsWidget, HasHorizontalAlignment, HasText, HasWordWrap, HasDirection, HasClickHandlers, HasDoubleClickHandlers,
  HasAllMouseHandlers {

}
