package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.KeyDownEvent;

public class DummyKeyDownEvent extends KeyDownEvent {

  public boolean prevented = false;
  private final int keyCode;

  public DummyKeyDownEvent(int keyCode) {
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
}
