package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.KeyPressEvent;

public class DummyKeyPressEvent extends KeyPressEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}