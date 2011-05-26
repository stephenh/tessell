package org.gwtmpv.model.dsl;

import com.google.gwt.event.shared.HandlerRegistration;

/** Wraps multiple {@link HandlerRegistration}s and can revoke them all at once. */
public class HandlerRegistrations {

  private final HandlerRegistration[] hrs;

  public HandlerRegistrations(HandlerRegistration... hrs) {
    this.hrs = hrs;
  }

  public void remove() {
    for (HandlerRegistration hr : hrs) {
      if (hr != null) {
        hr.removeHandler();
      }
    }
  }

}
