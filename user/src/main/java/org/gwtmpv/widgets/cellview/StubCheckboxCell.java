package org.gwtmpv.widgets.cellview;

public class StubCheckboxCell extends StubCell<Boolean> implements IsCheckboxCell {

  public void check(int displayedIndex) {
    if (isChecked(displayedIndex)) {
      throw new IllegalStateException(this + " is already checked");
    }
    setValue(displayedIndex, true);
  }

  public void uncheck(int displayedIndex) {
    if (!isChecked(displayedIndex)) {
      throw new IllegalStateException(this + " is not checked");
    }
    setValue(displayedIndex, false);
  }

  public boolean isChecked(int displayedIndex) {
    return getValue(displayedIndex) != null && getValue(displayedIndex).booleanValue();

  }

}
