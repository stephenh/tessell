package org.tessell.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.HasCell;

public class GwtCompositeCell<C> extends CompositeCell<C> implements IsCompositeCell<C> {

  public GwtCompositeCell(List<HasCell<C, ?>> hasCells) {
    super(hasCells);
  }

}
