package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.KeyPressEvent;

public class DummyKeyPressEvent extends KeyPressEvent {
  public boolean prevented = false;
  public boolean stopped = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }

  @Override
  public void stopPropagation() {
    stopped = true;
  }
}
