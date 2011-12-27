package org.tessell.widgets.cellview;

public class StubClickableTextCell extends StubCell<String> implements IsClickableTextCell {

  public void click(int displayedIndex) {
    // seems odd...
    setValue(displayedIndex, getValue(displayedIndex));
  }

}
