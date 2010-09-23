package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtTabLayoutPanel extends TabLayoutPanel implements IsTabLayoutPanel {

  public GwtTabLayoutPanel(double barHeight, Unit barUnit) {
    super(barHeight, barUnit);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public void add(IsWidget w) {
    add(w.asWidget());
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    throw new UnsupportedOperationException("not implemented");
  }

  @Override
  public boolean remove(IsWidget w) {
    return remove(w.asWidget());
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return getWidgetIndex(child.asWidget());
  }

  @Override
  public void add(IsWidget w, String tabText) {
    add(w.asWidget(), tabText);
  }

  @Override
  public void add(IsWidget w, String tabText, boolean tabIsHtml) {
    add(w.asWidget(), tabText, tabIsHtml);
  }

  @Override
  public void add(IsWidget w, IsWidget tabWidget) {
    add(w.asWidget(), tabWidget.asWidget());
  }

  @Override
  public IsWidget getTabIsWidget(IsWidget child) {
    return (IsWidget) getTabWidget(child.asWidget());
  }

  @Override
  public void insert(IsWidget child, int beforeIndex) {
    insert(child.asWidget(), beforeIndex);
  }

  @Override
  public void insert(IsWidget child, String tabText, boolean tabIsHtml, int beforeIndex) {
    // TODO Auto-generated method stub
  }

  @Override
  public void insert(IsWidget child, String tabText, int beforeIndex) {
    // TODO Auto-generated method stub

  }

  @Override
  public void insert(IsWidget child, IsWidget tabWidget, int beforeIndex) {
    // TODO Auto-generated method stub

  }

  @Override
  public void selectTab(IsWidget child) {
    // TODO Auto-generated method stub

  }

  @Override
  public void selectTab(IsWidget child, boolean fireEvents) {
    // TODO Auto-generated method stub

  }

  @Override
  public IsWidget getIsWidget(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IsWidget getTabIsWidget(int index) {
    return (IsWidget) super.getTabWidget(index);
  }

}
