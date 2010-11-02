package org.gwtmpv.widgets.cellview;

public class StubCheckboxCell extends StubCell<Boolean> implements IsCheckboxCell {

  public void check(int displayedIndex) {
    if (isChecked(displayedIndex)) {
      throw new IllegalStateException(this + " is already checked");
    }
  }

  public boolean isChecked(int displayedIndex) {
    return getValue(0) != null && getValue(0).booleanValue();

  }

}
