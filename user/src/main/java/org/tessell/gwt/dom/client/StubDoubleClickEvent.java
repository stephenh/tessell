package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.DoubleClickEvent;

public class StubDoubleClickEvent extends DoubleClickEvent {
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
