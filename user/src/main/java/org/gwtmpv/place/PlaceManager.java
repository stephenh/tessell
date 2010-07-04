package org.gwtmpv.place;

/**
 * Place managers work as an intermediary between the GWT {@link com.google.gwt.user.client.History} API and
 * {@link Place}s. It sets up event listener relationships to synchronize them.
 * 
 * @author David Peterson
 */
public interface PlaceManager {

  /**
   * Fires an event for the current place.
   * 
   * @return <code>false</code> if there was no current place to fire.
   */
  public boolean fireCurrentPlace();

  /**
   * Registers the place with the manager.
   * 
   * This allows the place to be updated when the browser's history token is updated.
   * 
   * @param place The place to register.
   */
  void registerPlace(Place place);

  /**
   * Deregisters the place from the manager.
   * 
   * @param place The place to deregister.
   */
  void deregisterPlace(Place place);
}
