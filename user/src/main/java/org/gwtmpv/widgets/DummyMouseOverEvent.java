package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.MouseOverEvent;

public class DummyMouseOverEvent extends MouseOverEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}