package org.gwtmpv.widgets;

public class StubTextArea extends StubTextBoxBase implements IsTextArea {

  private int characterWidth;
  private int visibleLines;
  private Direction direction;

  @Override
  public int getCharacterWidth() {
    return characterWidth;
  }

  @Override
  public int getVisibleLines() {
    return visibleLines;
  }

  @Override
  public void setCharacterWidth(final int characterWidth) {
    this.characterWidth = characterWidth;
  }

  @Override
  public void setVisibleLines(final int visibleLines) {
    this.visibleLines = visibleLines;
  }

  @Override
  public Direction getDirection() {
    return direction;
  }

  @Override
  public void setDirection(final Direction direction) {
    this.direction = direction;
  }

}
