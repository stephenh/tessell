package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.MouseOverEvent;

public class StubMouseOverEvent extends MouseOverEvent {
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
