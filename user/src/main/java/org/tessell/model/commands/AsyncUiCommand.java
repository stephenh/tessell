package org.tessell.model.commands;

import static org.tessell.model.properties.NewProperty.booleanProperty;

import org.tessell.model.properties.BooleanProperty;

/**
 * A base class for {@link UiCommand}s that are asynchronous, e.g. send AJAX requests.
 *
 * This basically exposes an "active" property for binding against when the request is in-flight.
 */
public abstract class AsyncUiCommand extends UiCommand implements HasActive {

  protected final BooleanProperty active = booleanProperty("active", false);

  /** @return whether the command is currently active */
  public BooleanProperty active() {
    return active;
  }

}
