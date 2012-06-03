package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

import org.tessell.widgets.IsWidget;

public interface HasIsWidgets {

  void add(IsWidget w);

  void clear();

  Iterator<IsWidget> iteratorIsWidgets();

  boolean remove(IsWidget w);

}
