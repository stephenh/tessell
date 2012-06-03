package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.IsElement;
import org.tessell.widgets.IsWidget;

public interface IsHTMLPanel extends IsComplexPanel {

  void add(IsWidget widget, IsElement elem);

  void addAndReplaceElement(IsWidget widget, IsElement elem);

  void addAndReplaceElement(IsWidget widget, String id);

}
