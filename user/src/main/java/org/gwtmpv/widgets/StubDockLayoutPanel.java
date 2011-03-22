package org.gwtmpv.widgets;

import com.google.gwt.layout.client.Layout.AnimationCallback;

public class StubDockLayoutPanel extends StubComplexPanel implements IsDockLayoutPanel {

  @Override
  public void onResize() {
  }

  @Override
  public void animate(int duration) {
  }

  @Override
  public void animate(int duration, AnimationCallback callback) {
  }

  @Override
  public void forceLayout() {
  }

  @Override
  public void addEast(IsWidget widget, double size) {
    add(widget);
  }

  @Override
  public void addLineEnd(IsWidget widget, double size) {
    add(widget);
  }

  @Override
  public void addLineStart(IsWidget widget, double size) {
    add(widget);
  }

  @Override
  public void addNorth(IsWidget widget, double size) {
    add(widget);
  }

  @Override
  public void addSouth(IsWidget widget, double size) {
    add(widget);
  }

  @Override
  public void addWest(IsWidget widget, double size) {
    add(widget);
  }

  @Override
  public void insertEast(IsWidget widget, double size, IsWidget before) {
    add(widget);
  }

  @Override
  public void insertLineEnd(IsWidget widget, double size, IsWidget before) {
    add(widget);
  }

  @Override
  public void insertLineStart(IsWidget widget, double size, IsWidget before) {
    add(widget);
  }

  @Override
  public void insertNorth(IsWidget widget, double size, IsWidget before) {
    add(widget);
  }

  @Override
  public void insertSouth(IsWidget widget, double size, IsWidget before) {
    add(widget);
  }

  @Override
  public void insertWest(IsWidget widget, double size, IsWidget before) {
    add(widget);
  }

}
