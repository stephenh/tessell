package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;

public class DummyMouseOutEvent extends MouseOutEvent {
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
