package org.gwtmpv.widgets;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtml;

public class StubCellList<T> extends AbstractStubHasDataWidget<T> implements IsCellList<T> {

  @SuppressWarnings("unused")
  private final Cell<T> cell;
  private SafeHtml emptyListMessage;

  public StubCellList(Cell<T> cell) {
    this.cell = cell;
  }

  @Override
  public SafeHtml getEmptyListMessage() {
    return emptyListMessage;
  }

  @Override
  public void setEmptyListMessage(SafeHtml safeHtml) {
    this.emptyListMessage = safeHtml;
  }

}
