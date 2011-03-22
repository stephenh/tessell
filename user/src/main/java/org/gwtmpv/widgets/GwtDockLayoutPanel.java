package org.gwtmpv.widgets;

import java.util.Iterator;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class GwtDockLayoutPanel extends DockLayoutPanel implements IsDockLayoutPanel {

  public GwtDockLayoutPanel(Unit unit) {
    super(unit);
  }

  @Override
  public boolean remove(IsWidget isWidget) {
    return remove(isWidget.asWidget());
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
    return new GwtIsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public int getWidgetIndex(IsWidget child) {
    return getWidgetIndex(child.asWidget());
  }

  @Override
  public void add(IsWidget widget) {
    add(widget.asWidget());
  }

  @Override
  public void addEast(IsWidget widget, double size) {
    addEast(widget.asWidget(), size);
  }

  @Override
  public void addLineEnd(IsWidget widget, double size) {
    addLineEnd(widget.asWidget(), size);
  }

  @Override
  public void addLineStart(IsWidget widget, double size) {
    addLineStart(widget.asWidget(), size);
  }

  @Override
  public void addNorth(IsWidget widget, double size) {
    addNorth(widget.asWidget(), size);
  }

  @Override
  public void addSouth(IsWidget widget, double size) {
    addSouth(widget.asWidget(), size);
  }

  @Override
  public void addWest(IsWidget widget, double size) {
    addWest(widget.asWidget(), size);
  }

  @Override
  public void insertEast(IsWidget widget, double size, IsWidget before) {
    insertEast(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertLineEnd(IsWidget widget, double size, IsWidget before) {
    insertLineEnd(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertLineStart(IsWidget widget, double size, IsWidget before) {
    insertLineStart(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertNorth(IsWidget widget, double size, IsWidget before) {
    insertNorth(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertSouth(IsWidget widget, double size, IsWidget before) {
    insertSouth(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertWest(IsWidget widget, double size, IsWidget before) {
    insertWest(widget.asWidget(), size, before.asWidget());
  }

}
