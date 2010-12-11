package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;

public class DummyMouseOutEvent extends MouseOutEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}