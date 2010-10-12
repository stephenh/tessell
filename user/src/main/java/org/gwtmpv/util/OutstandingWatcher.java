package org.gwtmpv.util;

import org.gwtmpv.dispatch.client.events.DispatchActionEvent;
import org.gwtmpv.dispatch.client.events.DispatchActionHandler;
import org.gwtmpv.dispatch.client.events.DispatchFailureEvent;
import org.gwtmpv.dispatch.client.events.DispatchFailureHandler;
import org.gwtmpv.dispatch.client.events.DispatchResultEvent;
import org.gwtmpv.dispatch.client.events.DispatchResultHandler;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasText;

/** Updates {@link HasText} elements on the screen with the outstanding/total dispatch actions, for testing. */
public class OutstandingWatcher {

  private final HasText outstandingText;
  private final HasText totalText;
  private int outstanding;
  private int total;

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

  /** Increment outstanding. */
  private class OnAction implements DispatchActionHandler {
    public void onDispatchAction(DispatchActionEvent event) {
      outstandingText.setText(Integer.toString(++outstanding));
      totalText.setText(Integer.toString(++total));
    }
  }

  /** Decrement outstanding. */
  private class OnResult implements DispatchResultHandler {
    public void onDispatchResult(DispatchResultEvent event) {
      outstandingText.setText(Integer.toString(--outstanding));
    }
  }

  /** Decrement outstanding. */
  private class OnFailure implements DispatchFailureHandler {
    public void onDispatchFailure(DispatchFailureEvent event) {
      outstandingText.setText(Integer.toString(--outstanding));
    }
  }

}
