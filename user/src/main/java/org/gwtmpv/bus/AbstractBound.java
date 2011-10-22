package org.gwtmpv.bus;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerRegistration;

/** A basic presenter that tracks bound handler registrations. */
public abstract class AbstractBound implements Bound {

  private boolean bound = false;
  private boolean hasBeenUnbound = false;
  private final ArrayList<com.google.web.bindery.event.shared.HandlerRegistration> registrations = new ArrayList<com.google.web.bindery.event.shared.HandlerRegistration>();

  @Override
  public final void bind() {
    if (hasBeenUnbound) {
      throw new IllegalStateException("This instance has already been unbound " + this);
    }
    if (!bound) {
      onBind();
      if (!bound) {
        throw new IllegalStateException("A subclass forgot to call super.onBind for " + this);
      }
    }
  }

  @Override
  public final void unbind() {
    if (bound) {
      onUnbind();
      if (bound) {
        throw new IllegalStateException("A subclass forgot to call super.onUnbind for " + this);
      }
      hasBeenUnbound = true;
    }
  }

  /** @return whether we have been bound yet */
  public boolean isBound() {
    return bound;
  }

  /** Registers a handler to be removed on {@link #unbind()}. */
  public final void registerHandler(final HandlerRegistration handlerRegistration) {
    registrations.add(handlerRegistration);
  }

  /** Registers a handler to be removed on {@link #unbind()}. */
  public final void registerHandler(final com.google.web.bindery.event.shared.HandlerRegistration handlerRegistration) {
    registrations.add(handlerRegistration);
  }

  /** Register handlers to be removed on {@link #unbind()}. */
  public final void registerHandlers(final HandlerRegistration... handlerRegistrations) {
    for (final HandlerRegistration handlerRegistration : handlerRegistrations) {
      registrations.add(handlerRegistration);
    }
  }

  /** Register handlers to be removed on {@link #unbind()}. */
  protected final void registerHandlers(final ArrayList<HandlerRegistration> handlerRegistrations) {
    for (final HandlerRegistration handlerRegistration : handlerRegistrations) {
      registrations.add(handlerRegistration);
    }
  }

  /** This method is called when binding the instance. */
  protected void onBind() {
    bound = true; // set here so we can catch subclasses not calling super.onBind
  }

  /** This method is called when unbinding the instance. */
  protected void onUnbind() {
    bound = false; // set here so we can catch subclasses not calling super.onUnbind
    for (final com.google.web.bindery.event.shared.HandlerRegistration registration : registrations) {
      registration.removeHandler();
    }
    registrations.clear();
  }

}
