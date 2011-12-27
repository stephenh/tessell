package org.tessell.widgets.cellview;

import com.google.gwt.cell.client.Cell;

public interface IsHyperlinkCell extends Cell<IsHyperlinkCell.Data> {

  /** Data class for the column value to pass into the cell. */
  public static class Data {
    public final String href;
    public final String content;

    public Data(String href, String content) {
      this.href = href;
      this.content = content;
    }
  }

}
