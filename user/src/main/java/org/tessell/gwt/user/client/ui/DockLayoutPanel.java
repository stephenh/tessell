package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;

import com.google.gwt.dom.client.Style.Unit;

public class DockLayoutPanel extends com.google.gwt.user.client.ui.DockLayoutPanel implements IsDockLayoutPanel {

  public DockLayoutPanel(Unit unit) {
    super(unit);
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsWidget getIsParent() {
    return (IsWidget) getParent();
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public Iterator<IsWidget> iteratorIsWidgets() {
    return new IsWidgetIteratorAdaptor(iterator());
  }

  @Override
  public IsWidget getIsWidget(int index) {
    return (IsWidget) getWidget(index);
  }

  @Override
  public void addLineEnd(com.google.gwt.user.client.ui.IsWidget widget, double size) {
    addLineEnd(widget.asWidget(), size);
  }

  @Override
  public void addLineStart(com.google.gwt.user.client.ui.IsWidget widget, double size) {
    addLineStart(widget.asWidget(), size);
  }

  @Override
  public void insertEast(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before) {
    insertEast(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertLineEnd(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before) {
    insertLineEnd(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertLineStart(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before) {
    insertLineStart(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertNorth(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before) {
    insertNorth(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertSouth(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before) {
    insertSouth(widget.asWidget(), size, before.asWidget());
  }

  @Override
  public void insertWest(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before) {
    insertWest(widget.asWidget(), size, before.asWidget());
  }

}
