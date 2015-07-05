package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.DragEndEvent;

public class StubDragEndEvent extends DragEndEvent {
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
