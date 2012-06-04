package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.IsElement;

public interface IsHTMLPanel extends IsComplexPanel {

  void add(com.google.gwt.user.client.ui.IsWidget widget, IsElement elem);

  void addAndReplaceElement(com.google.gwt.user.client.ui.IsWidget widget, IsElement elem);

  void addAndReplaceElement(com.google.gwt.user.client.ui.IsWidget widget, String id);

}
