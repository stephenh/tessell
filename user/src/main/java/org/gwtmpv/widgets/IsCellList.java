package org.gwtmpv.widgets;

import java.util.List;

public interface IsCellList<T> extends IsWidget {

  void setData(int start, int length, List<T> values);

}
