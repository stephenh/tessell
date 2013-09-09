package org.tessell.gwt.user.client.ui;

public class StubTextBox extends StubTextBoxBase implements IsTextBox {

  @Override
  public int getVisibleLength() {
    return 0;
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
