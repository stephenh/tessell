package org.gwtmpv.widgets;

import org.gwtmpv.widgets.cellview.IsColumn;
import org.gwtmpv.widgets.cellview.IsHeader;

public interface IsCellTable<T> extends IsWidget, IsAbstractHasData<T> {

  void addColumn(IsColumn<T, ?> col);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header);

  void addColumn(IsColumn<T, ?> col, IsHeader<?> header, IsHeader<?> footer);

  void removeColumn(IsColumn<T, ?> col);

  void removeColumn(int index);

  void redrawHeaders();

  void redrawFooters();

}
