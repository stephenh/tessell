package org.tessell.util;

import org.tessell.dispatch.client.events.DispatchActionEvent;
import org.tessell.dispatch.client.events.DispatchActionHandler;
import org.tessell.dispatch.client.events.DispatchFailureEvent;
import org.tessell.dispatch.client.events.DispatchFailureHandler;
import org.tessell.dispatch.client.events.DispatchResultEvent;
import org.tessell.dispatch.client.events.DispatchResultHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.HasText;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/** Updates {@link HasText} elements on the screen with the outstanding/total dispatch actions, for testing. */
public class OutstandingWatcher {

  private final HasText outstandingText;
  private final HasText totalText;
  private int outstanding;
  private int total;
  private boolean isUpdateScheduled;

  public OutstandingWatcher(HasText outstandingText, HasText totalText) {
    this.outstandingText = outstandingText;
    this.totalText = totalText;
  }

  /** Starts listening on {@code bus}. */
  public HandlerRegistration[] listenOn(EventBus bus) {
    HandlerRegistration a = bus.addHandler(DispatchActionEvent.getType(), new OnAction());
    HandlerRegistration b = bus.addHandler(DispatchResultEvent.getType(), new OnResult());
    HandlerRegistration c = bus.addHandler(DispatchFailureEvent.getType(), new OnFailure());
    return new HandlerRegistration[] { a, b, c };
  }

  private void scheduleUpdate() {
    if (GWT.isClient()) {
      if (outstanding > 0) {
        // if outstanding > 0, we want to update the DOM right away
        update();
      } else if (!isUpdateScheduled) {
        // but if outstanding == 0, just because this request is done
        // doesn't mean another won't be started within this event loop
        // by some other business logic. to avoid selenium potentially
        // seems a very brief "0", defer this update until the current
        // loop is finished.
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          public void execute() {
            update();
            isUpdateScheduled = false;
          }
        });
        isUpdateScheduled = true;
      }
    } else {
      // during unit tests just update the view immediately
      update();
    }
  }

  private void update() {
    outstandingText.setText(Integer.toString(outstanding));
    totalText.setText(Integer.toString(total));
  }

  /** Increment total/outstanding. */
  private class OnAction implements DispatchActionHandler {
    public void onDispatchAction(DispatchActionEvent event) {
      outstanding++;
      total++;
      scheduleUpdate();
    }
  }

  /** Decrement outstanding. */
  private class OnResult implements DispatchResultHandler {
    public void onDispatchResult(DispatchResultEvent event) {
      outstanding--;
      scheduleUpdate();
    }
  }

  /** Decrement outstanding. */
  private class OnFailure implements DispatchFailureHandler {
    public void onDispatchFailure(DispatchFailureEvent event) {
      outstanding--;
      scheduleUpdate();
    }
  }

}
