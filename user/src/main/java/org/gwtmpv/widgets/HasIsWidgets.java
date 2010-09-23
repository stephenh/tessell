package org.gwtmpv.widgets;

import java.util.Iterator;

public interface HasIsWidgets {

  void add(IsWidget w);

  void clear();

  Iterator<IsWidget> iteratorIsWidgets();

  boolean remove(IsWidget w);

}
