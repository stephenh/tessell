package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.DragEnterEvent;

public class StubDragEnterEvent extends DragEnterEvent {
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
