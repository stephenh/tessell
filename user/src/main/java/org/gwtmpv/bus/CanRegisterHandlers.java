package org.gwtmpv.bus;

import com.google.gwt.event.shared.HandlerRegistration;

/** Interface for objects that can be responsible for {@link HandlerRegistration}s. */
public interface CanRegisterHandlers {

  void registerHandler(final HandlerRegistration handlerRegistration);

  void registerHandlers(final HandlerRegistration... handlerRegistrations);

}
