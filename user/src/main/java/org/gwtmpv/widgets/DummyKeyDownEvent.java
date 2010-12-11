package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.KeyDownEvent;

public class DummyKeyDownEvent extends KeyDownEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}