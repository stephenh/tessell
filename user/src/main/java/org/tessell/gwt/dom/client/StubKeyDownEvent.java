package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.KeyDownEvent;

public class StubKeyDownEvent extends KeyDownEvent {

  public boolean prevented = false;
  public boolean stopped = false;
  private final int keyCode;

  public StubKeyDownEvent(int keyCode) {
    this.keyCode = keyCode;
  }

  @Override
  public int getNativeKeyCode() {
    return keyCode;
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
