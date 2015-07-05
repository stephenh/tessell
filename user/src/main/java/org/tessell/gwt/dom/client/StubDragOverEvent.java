package org.tessell.gwt.dom.client;

import com.google.gwt.event.dom.client.DragOverEvent;

public class StubDragOverEvent extends DragOverEvent {

  public boolean prevented = false;
  public boolean stopped = false;

  // We don't override getData here because Chrome doesn't allow
  // getData to be called in dragover anyway:
  // http://stackoverflow.com/questions/7830249/how-do-you-get-the-selected-items-from-a-dragstart-event-in-chrome-is-datatrans

  @Override
  public void preventDefault() {
    prevented = true;
  }

  @Override
  public void stopPropagation() {
    stopped = true;
  }
}
