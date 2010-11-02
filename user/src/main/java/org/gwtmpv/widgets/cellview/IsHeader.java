package org.gwtmpv.widgets.cellview;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.cellview.client.Header;

public interface IsHeader<C> {

  Cell<C> getCell();

  C getValue();

  ValueUpdater<C> getUpdater();

  Header<C> asHeader();

}
