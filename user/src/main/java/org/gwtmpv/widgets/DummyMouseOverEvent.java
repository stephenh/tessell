package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.MouseOverEvent;

public class DummyMouseOverEvent extends MouseOverEvent {
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
