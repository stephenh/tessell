package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.FocusEvent;

public class DummyFocusEvent extends FocusEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}