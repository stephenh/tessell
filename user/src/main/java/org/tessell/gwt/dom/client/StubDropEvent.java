package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.DropEvent;

public class StubDropEvent extends DropEvent {

  public boolean prevented = false;
  public boolean stopped = false;
  private final String mimeType;
  private final String data;

  public StubDropEvent(String mimeType, String data) {
    this.mimeType = mimeType;
    this.data = data;
  }

  @Override
  public String getData(String mimeType) {
    if (this.mimeType.equals(mimeType)) {
      return data;
    }
    return null;
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
