package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.ClickEvent;

public class DummyClickEvent extends ClickEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}