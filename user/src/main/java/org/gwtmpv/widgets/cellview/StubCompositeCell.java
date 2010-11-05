package org.gwtmpv.widgets.cellview;

import java.util.List;

import com.google.gwt.cell.client.HasCell;

public class StubCompositeCell<C> extends StubCell<C> implements IsCompositeCell<C> {

  private final List<HasCell<C, ?>> cells;

  public StubCompositeCell(List<HasCell<C, ?>> cells) {
    this.cells = cells;
  }

  public List<HasCell<C, ?>> getCells() {
    return cells;
  }

}
