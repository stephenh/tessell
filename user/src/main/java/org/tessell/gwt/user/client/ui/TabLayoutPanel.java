package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsWidget;
import org.tessell.widgets.OtherTypes;

import com.google.gwt.dom.client.Style.Unit;

@OtherTypes(intf = IsTabLayoutPanel.class, stub = StubTabLayoutPanel.class)
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
    insert(child.asWidget(), tabText, tabIsHtml, beforeIndex);
  }

  @Override
  public void insert(IsWidget child, String tabText, int beforeIndex) {
    insert(child.asWidget(), tabText, beforeIndex);
  }

  @Override
  public void insert(IsWidget child, IsWidget tabWidget, int beforeIndex) {
    insert(child.asWidget(), tabWidget.asWidget(), beforeIndex);
  }

  @Override
  public void selectTab(IsWidget child) {
    selectTab(child.asWidget());
  }

  @Override
  public void selectTab(IsWidget child, boolean fireEvents) {
    selectTab(child.asWidget(), fireEvents);
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public IsWidget getTabIsWidget(int index) {
    return (IsWidget) super.getTabWidget(index);
  }

}
