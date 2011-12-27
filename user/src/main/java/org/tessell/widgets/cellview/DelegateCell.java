package org.tessell.widgets.cellview;

import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class DelegateCell<C> implements Cell<C> {

  private final Cell<C> delegate;

  public DelegateCell(Cell<C> delegate) {
    this.delegate = delegate;
  }

  public Cell<C> getDelegate() {
    return delegate;
  }

  @Override
  public boolean dependsOnSelection() {
    return delegate.dependsOnSelection();
  }

  @Override
  public Set<String> getConsumedEvents() {
    return delegate.getConsumedEvents();
  }

  @Override
  public boolean handlesSelection() {
    return delegate.handlesSelection();
  }

  @Override
  public boolean isEditing(Cell.Context context, Element parent, C value) {
    return delegate.isEditing(context, parent, value);
  }

  @Override
  public void onBrowserEvent(Cell.Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    delegate.onBrowserEvent(context, parent, value, event, valueUpdater);
  }

  @Override
  public void render(Cell.Context context, C value, SafeHtmlBuilder sb) {
    delegate.render(context, value, sb);
  }

  @Override
  public boolean resetFocus(Cell.Context context, Element parent, C value) {
    return delegate.resetFocus(context, parent, value);
  }

  @Override
  public void setValue(Cell.Context context, Element parent, C value) {
    delegate.setValue(context, parent, value);
  }

}
