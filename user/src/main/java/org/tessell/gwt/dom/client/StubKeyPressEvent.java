package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.KeyPressEvent;

public class StubKeyPressEvent extends KeyPressEvent {

  public boolean prevented = false;
  public boolean stopped = false;

  private final char c;
  private final StubKeyModifiers modifiers;

  public StubKeyPressEvent(char c, StubKeyModifiers modifiers) {
    this.c = c;
    this.modifiers = modifiers;
  }

  @Override
  public char getCharCode() {
    return c;
  }

  @Override
  public int getUnicodeCharCode() {
    return c;
  }

  @Override
  public boolean isAltKeyDown() {
    return modifiers.alt;
  }

  @Override
  public boolean isControlKeyDown() {
    return modifiers.control;
  }

  @Override
  public boolean isMetaKeyDown() {
    return modifiers.meta;
  }

  @Override
  public boolean isShiftKeyDown() {
    return modifiers.shift;
  }

  @Override
  public void preventDefault() {
    prevented = true;
  }

  @Override
  public void stopPropagation() {
    stopped = true;
  }
}
