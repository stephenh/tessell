package org.tessell.bus;

import java.util.ArrayList;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * A base class for types that implement {@link Bound}.
 *
 * Provides basic {@link #bind()}/{@link #unbind()} implementations that enforce
 * the {@code super.onBind()}/@code super.onUnbind()} conventions.
 */
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
  @Override
  public final boolean isBound() {
    return bound;
  }

  /** Registers a handler to be removed on {@link #unbind()}. */
  protected void registerHandler(final HandlerRegistration handlerRegistration) {
    registrations.add(handlerRegistration);
  }

  /** Registers a handler to be removed on {@link #unbind()}. */
  protected void registerHandler(final com.google.web.bindery.event.shared.HandlerRegistration handlerRegistration) {
    registrations.add(handlerRegistration);
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
