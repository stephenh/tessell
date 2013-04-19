package org.tessell.bootstrap;

import com.google.gwt.event.dom.client.KeyDownHandler;

/** Marks a line as being able to trigger the default form action. */
public interface CanTriggerDefaultAction {

  void addDefaultActionHandler(KeyDownHandler handler);

}
