package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;

/** For wrapping the {@link RootPanel} instance in {@link IsAbsolutePanel}. */
public class GwtAbsolutePanelDelegate extends GwtWidgetDelegate implements IsAbsolutePanel {

  private final AbsolutePanel delegate;

  public GwtAbsolutePanelDelegate(AbsolutePanel delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  @Override
  public void add(com.google.gwt.user.client.ui.IsWidget isWidget) {
    delegate.add(isWidget.asWidget());
  }

  @Override
  public boolean remove(com.google.gwt.user.client.ui.IsWidget isWidget) {
    return delegate.remove(isWidget.asWidget());
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new IsWidgetIteratorAdaptor(delegate.iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) delegate.getWidget(index);
  }

  @Override
  public int getWidgetCount() {
    return delegate.getWidgetCount();
  }

  @Override
  public int getWidgetIndex(com.google.gwt.user.client.ui.IsWidget child) {
    return delegate.getWidgetIndex(child.asWidget());
  }

  @Override
  public boolean remove(int index) {
    return delegate.remove(index);
  }

  @Override
  public void add(com.google.gwt.user.client.ui.IsWidget w, int left, int top) {
    delegate.add(w.asWidget(), left, top);
  }

  @Override
  public int getIsWidgetLeft(com.google.gwt.user.client.ui.IsWidget w) {
    return delegate.getWidgetLeft(w.asWidget());
  }

  @Override
  public int getIsWidgetTop(com.google.gwt.user.client.ui.IsWidget w) {
    return delegate.getWidgetTop(w.asWidget());
  }

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget w, int beforeIndex) {
    delegate.insert(w.asWidget(), beforeIndex);
  }

  @Override
  public void insert(com.google.gwt.user.client.ui.IsWidget w, int left, int top, int beforeIndex) {
    delegate.insert(w.asWidget(), left, top, beforeIndex);
  }

  @Override
  public void setWidgetPosition(com.google.gwt.user.client.ui.IsWidget w, int left, int top) {
    delegate.setWidgetPosition(w.asWidget(), left, top);
  }

}
