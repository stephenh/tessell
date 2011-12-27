package org.tessell.widgets;

import com.google.gwt.event.dom.client.KeyUpEvent;

public class DummyKeyUpEvent extends KeyUpEvent {

  public boolean prevented = false;
  public boolean stopped = false;
  private final int keyCode;

  public DummyKeyUpEvent(int keyCode) {
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
