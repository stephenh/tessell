package org.gwtmpv.widgets;

import java.util.List;

public interface IsCellList<T> extends IsWidget {

  void setRowData(int start, List<? extends T> values);

}
