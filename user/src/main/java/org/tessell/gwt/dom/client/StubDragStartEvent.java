package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.DragStartEvent;

public class StubDragStartEvent extends DragStartEvent {
  public boolean prevented = false;
  public boolean stopped = false;
  public String mimeType = null;
  public String data = null;

  @Override
  public void setData(String mimeType, String data) {
    this.mimeType = mimeType;
    this.data = data;
  }

  @Override
  public void preventDefault() {
    prevented = true;
  }

  @Override
  public void stopPropagation() {
    stopped = true;
  }
}
