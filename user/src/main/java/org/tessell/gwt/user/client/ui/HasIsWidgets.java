package org.tessell.gwt.user.client.ui;

import java.util.Iterator;

public interface HasIsWidgets {

  void add(com.google.gwt.user.client.ui.IsWidget w);

  void clear();

  Iterator<IsWidget> iteratorIsWidgets();

  boolean remove(com.google.gwt.user.client.ui.IsWidget w);

}
