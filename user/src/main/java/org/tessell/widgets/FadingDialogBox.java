package org.tessell.widgets;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.gwt.user.client.ui.IsWidgetIteratorAdaptor;

public class FadingDialogBox extends com.google.gwt.user.client.ui.FadingDialogBox implements IsFadingDialogBox {

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
