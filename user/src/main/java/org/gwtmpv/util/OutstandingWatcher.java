package org.gwtmpv.util;

import org.gwtmpv.dispatch.client.events.DispatchActionEvent;
import org.gwtmpv.dispatch.client.events.DispatchActionHandler;
import org.gwtmpv.dispatch.client.events.DispatchFailureEvent;
import org.gwtmpv.dispatch.client.events.DispatchFailureHandler;
import org.gwtmpv.dispatch.client.events.DispatchResultEvent;
import org.gwtmpv.dispatch.client.events.DispatchResultHandler;

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
    // to ensure any ajax results that lead to more requests
    // don't temporarily bounce the outstanding to zero, defer
    // updating the view until leaving the event loop
    if (GWT.isClient()) {
      if (!isUpdateScheduled) {
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
