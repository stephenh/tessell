package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.user.client.ui.AbsolutePanel;

public class GwtAbsolutePanelDelegate extends GwtWidgetDelegate implements IsAbsolutePanel {

  private final AbsolutePanel delegate;

  public GwtAbsolutePanelDelegate(AbsolutePanel delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  @Override
  public void add(IsWidget isWidget) {
    delegate.add(isWidget.asWidget());
  }

  @Override
  public boolean remove(IsWidget isWidget) {
    return delegate.remove(isWidget.asWidget());
  }

  @Override
  public void clear() {
    delegate.clear();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new GwtIsWidgetIteratorAdaptor(delegate.iterator());
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
  public int getWidgetIndex(IsWidget child) {
    return delegate.getWidgetIndex(child.asWidget());
  }

  @Override
  public boolean remove(int index) {
    return delegate.remove(index);
  }

  @Override
  public void add(IsWidget w, int left, int top) {
    delegate.add(w.asWidget(), left, top);
  }

  @Override
  public int getIsWidgetLeft(IsWidget w) {
    return delegate.getWidgetLeft(w.asWidget());
  }

  @Override
  public int getIsWidgetTop(IsWidget w) {
    return delegate.getWidgetTop(w.asWidget());
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    delegate.insert(w.asWidget(), beforeIndex);
  }

  @Override
  public void insert(IsWidget w, int left, int top, int beforeIndex) {
    delegate.insert(w.asWidget(), left, top, beforeIndex);
  }

  @Override
  public void setWidgetPosition(IsWidget w, int left, int top) {
    delegate.setWidgetPosition(w.asWidget(), left, top);
  }

}
