package org.gwtmpv.place;

import org.gwtmpv.bus.AbstractBound;
import org.gwtmpv.bus.EventBus;
import org.gwtmpv.place.events.PlaceChangedEvent;

/**
 * A place represents a particular bookmark in an application.
 * 
 * A place is stateful - it may represent a location with it's current settings, such as a particular ID value, or other
 * unique indicators that will allow a user to track back to that location later, either via a browser bookmark, or by
 * clicking the 'back' button.
 */
public abstract class Place extends AbstractBound {

  protected final EventBus eventBus;
  protected final String name;

  /**
   * @param name
   *          the unique name for this place
   */
  public Place(final EventBus eventBus, final String name) {
    this.eventBus = eventBus;
    this.name = name;
  }

  /** @return the unique name for this place */
  public final String getName() {
    return name;
  }

  /** This method is for sub-classes to execute logic for a matched request. */
  public abstract void handleRequest(PlaceRequest request);

  @Override
  public String toString() {
    return getName();
  }

  /** Subclasses should call when they are changed and the PlaceManager should update the history token. */
  protected void firePlaceChanged(final PlaceRequest request) {
    eventBus.fireEvent(new PlaceChangedEvent(this, request));
  }

}
