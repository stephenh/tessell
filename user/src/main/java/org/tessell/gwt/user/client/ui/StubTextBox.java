package org.tessell.gwt.user.client.ui;


public class StubTextBox extends StubTextBoxBase implements IsTextBox {

  private int maxLength;

  @Override
  public int getMaxLength() {
    return maxLength;
  }

  @Override
  public int getVisibleLength() {
    return 0;
  }

  @Override
  public void setMaxLength(final int length) {
    maxLength = length;
  }

  @Override
  public void setValue(final String value, final boolean fireEvents) {
    if (value != null && maxLength > 0 && value.length() > maxLength) {
      super.setValue(value.substring(0, maxLength), fireEvents);
    } else {
      super.setValue(value, fireEvents);
    }
  }

  @Override
  public void setVisibleLength(final int length) {
  }

  @Override
  public Direction getDirection() {
    return null;
  }

  @Override
  public void setDirection(final Direction direction) {
  }
}