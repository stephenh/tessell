package org.gwtmpv.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubTabLayoutPanel extends StubWidget implements IsTabLayoutPanel {

  private final List<IsWidget> widgets = new ArrayList<IsWidget>();
  private final List<Object> tabs = new ArrayList<Object>();
  private int selected = -1;

  @Override
  public void add(IsWidget w) {
    widgets.add(w);
    tabs.add("none");
  }

  @Override
  public void clear() {
    widgets.clear();
    tabs.clear();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return widgets.iterator();
  }

  @Override
  public boolean remove(IsWidget w) {
    tabs.remove(widgets.indexOf(w));
    return widgets.remove(w);
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return widgets.get(index);
  }

  @Override
  public int getWidgetCount() {
    return widgets.size();
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return widgets.indexOf(child);
  }

  @Override
  public boolean remove(int index) {
    tabs.remove(index);
    return widgets.remove(index) != null;
  }

  @Override
  public void add(IsWidget w, String tabText) {
    add(w);
    tabs.add(tabText);
  }

  @Override
  public void add(IsWidget w, String tabText, boolean tabIsHtml) {
    add(w);
    tabs.add(tabText);
  }

  @Override
  public void add(IsWidget w, IsWidget tabWidget) {
    add(w);
    tabs.add(tabWidget);
  }

  @Override
  public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Integer> handler) {
    return handlers.addHandler(BeforeSelectionEvent.getType(), handler);
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
    return handlers.addHandler(SelectionEvent.getType(), handler);
  }

  @Override
  public int getSelectedIndex() {
    return selected;
  }

  @Override
  public IsWidget getTabIsWidget(int index) {
    return (IsWidget) tabs.get(index);
  }

  @Override
  public IsWidget getTabIsWidget(IsWidget child) {
    return (IsWidget) tabs.get(widgets.indexOf(child));
  }

  @Override
  public void insert(IsWidget child, int beforeIndex) {
    widgets.add(beforeIndex, child);
    tabs.add(beforeIndex, "none");
  }

  @Override
  public void insert(IsWidget child, String tabText, boolean tabIsHtml, int beforeIndex) {
    widgets.add(beforeIndex, child);
    tabs.add(beforeIndex, tabText);
  }

  @Override
  public void insert(IsWidget child, String tabText, int beforeIndex) {
    widgets.add(beforeIndex, child);
    tabs.add(beforeIndex, tabText);
  }

  @Override
  public void insert(IsWidget child, IsWidget tabWidget, int beforeIndex) {
    widgets.add(beforeIndex, child);
    tabs.add(beforeIndex, tabWidget);
  }

  @Override
  public void selectTab(int index) {
    selectTab(index, true);
  }

  @Override
  public void selectTab(int index, boolean fireEvents) {
    if (fireEvents) {
      BeforeSelectionEvent<Integer> e = BeforeSelectionEvent.fire(this, index);
      if (e != null && e.isCanceled()) {
        return;
      }
    }

    selected = index;

    SelectionEvent.fire(this, index);
  }

  @Override
  public void selectTab(IsWidget child) {
    selectTab(widgets.indexOf(child));
  }

  @Override
  public void selectTab(IsWidget child, boolean fireEvents) {
    selectTab(widgets.indexOf(child), true);
  }

  @Override
  public void setTabHTML(int index, String html) {
    tabs.set(index, html);
  }

  @Override
  public void setTabText(int index, String text) {
    tabs.set(index, text);
  }

}
