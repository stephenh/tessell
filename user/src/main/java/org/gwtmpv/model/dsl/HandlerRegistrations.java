package org.gwtmpv.model.dsl;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

/** Wraps multiple {@link HandlerRegistration}s and can revoke them all at once. */
public class HandlerRegistrations {

  private final List<HandlerRegistration> hrs = new ArrayList<HandlerRegistration>();

  public HandlerRegistrations(HandlerRegistration... hrs) {
    for (HandlerRegistration hr : hrs) {
      this.hrs.add(hr);
    }
  }

  public void add(HandlerRegistration hr) {
    hrs.add(hr);
  }

  public void add(HandlerRegistrations hrs) {
    for (HandlerRegistration hr : hrs.hrs) {
      this.hrs.add(hr);
    }
  }

  public void remove() {
    for (HandlerRegistration hr : hrs) {
      if (hr != null) {
        hr.removeHandler();
      }
    }
  }

}
