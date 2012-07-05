package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

import com.google.gwt.dom.client.Style.Unit;

public class TabLayoutPanel extends com.google.gwt.user.client.ui.TabLayoutPanel implements IsTabLayoutPanel {

  public TabLayoutPanel(double barHeight, Unit barUnit) {
    super(barHeight, barUnit);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public IsWidget getTabIsWidget(com.google.gwt.user.client.ui.IsWidget child) {
    return (IsWidget) getTabWidget(child.asWidget());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public IsWidget getTabIsWidget(int index) {
    return (IsWidget) super.getTabWidget(index);
  }

  @Override
  public boolean remove(com.google.gwt.user.client.ui.IsWidget w) {
    return super.remove(w.asWidget());
  }

}
