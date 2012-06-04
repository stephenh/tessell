package org.tessell.gwt.user.client.ui;

import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ProvidesResize;

public interface IsTabLayoutPanel extends org.tessell.gwt.user.client.ui.IsWidget, HasIsWidgets, IsIndexedPanel, ProvidesResize,
  HasBeforeSelectionHandlers<Integer>, HasSelectionHandlers<Integer> {

  void add(com.google.gwt.user.client.ui.IsWidget w, String tabText);

  void add(com.google.gwt.user.client.ui.IsWidget w, String tabText, boolean tabIsHtml);

  void add(com.google.gwt.user.client.ui.IsWidget w, com.google.gwt.user.client.ui.IsWidget tabWidget);

  HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Integer> handler);

  HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler);

  int getSelectedIndex();

  IsWidget getTabIsWidget(int index);

  IsWidget getTabIsWidget(com.google.gwt.user.client.ui.IsWidget child);

  void insert(com.google.gwt.user.client.ui.IsWidget child, int beforeIndex);

  void insert(com.google.gwt.user.client.ui.IsWidget child, String tabText, boolean tabIsHtml, int beforeIndex);

  void insert(com.google.gwt.user.client.ui.IsWidget child, String tabText, int beforeIndex);

  void insert(com.google.gwt.user.client.ui.IsWidget child, com.google.gwt.user.client.ui.IsWidget tabWidget, int beforeIndex);

  void selectTab(int index);

  void selectTab(int index, boolean fireEvents);

  void selectTab(com.google.gwt.user.client.ui.IsWidget child);

  void selectTab(com.google.gwt.user.client.ui.IsWidget child, boolean fireEvents);

  void setTabHTML(int index, String html);

  void setTabText(int index, String text);

}
