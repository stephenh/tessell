package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.BlurEvent;

public class DummyBlurEvent extends BlurEvent {
  public boolean prevented = false;

  @Override
  public void preventDefault() {
    prevented = true;
  }
}