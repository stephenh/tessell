package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.KeyDownEvent;

public class StubKeyDownEvent extends KeyDownEvent {

  public boolean prevented = false;
  public boolean stopped = false;

  private final int keyCode;
  private final StubKeyModifiers modifiers;

  public StubKeyDownEvent(int keyCode, StubKeyModifiers modifiers) {
    this.keyCode = keyCode;
    this.modifiers = modifiers;
  }

  @Override
  public int getNativeKeyCode() {
    return keyCode;
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
