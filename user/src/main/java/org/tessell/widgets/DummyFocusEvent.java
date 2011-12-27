package org.tessell.widgets;

import com.google.gwt.event.dom.client.FocusEvent;

public class DummyFocusEvent extends FocusEvent {
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
