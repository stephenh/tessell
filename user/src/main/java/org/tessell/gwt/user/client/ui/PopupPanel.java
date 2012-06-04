package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsPopupPanel.class, stub = StubPopupPanel.class)
public class PopupPanel extends com.google.gwt.user.client.ui.PopupPanel implements IsPopupPanel {

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public void addAutoHidePartner(IsElement element) {
    addAutoHidePartner(element.asElement());
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new IsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget() {
    return (IsWidget) getWidget();
  }

}
