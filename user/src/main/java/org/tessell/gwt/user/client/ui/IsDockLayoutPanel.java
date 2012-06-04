package org.tessell.gwt.user.client.ui;

import com.google.gwt.user.client.ui.AnimatedLayout;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;

public interface IsDockLayoutPanel extends IsComplexPanel, RequiresResize, ProvidesResize, AnimatedLayout {

  void add(com.google.gwt.user.client.ui.IsWidget widget);

  void addEast(com.google.gwt.user.client.ui.IsWidget widget, double size);

  void addLineEnd(com.google.gwt.user.client.ui.IsWidget widget, double size);

  void addLineStart(com.google.gwt.user.client.ui.IsWidget widget, double size);

  void addNorth(com.google.gwt.user.client.ui.IsWidget widget, double size);

  void addSouth(com.google.gwt.user.client.ui.IsWidget widget, double size);

  void addWest(com.google.gwt.user.client.ui.IsWidget widget, double size);

  void insertEast(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before);

  void insertLineEnd(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before);

  void insertLineStart(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before);

  void insertNorth(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before);

  void insertSouth(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before);

  void insertWest(com.google.gwt.user.client.ui.IsWidget widget, double size, com.google.gwt.user.client.ui.IsWidget before);
}
