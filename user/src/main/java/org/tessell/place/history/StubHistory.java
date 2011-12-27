package org.tessell.place.history;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimplerEventBus;

public class StubHistory implements IsHistory, HasValueChangeHandlers<String> {

  private final EventBus handlers = new SimplerEventBus();
  private String token;

  public void setToken(final String token) {
    this.token = token;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    return handlers.addHandler(ValueChangeEvent.getType(), handler);
  }

  @Override
  public void fireCurrentHistoryState() {
    ValueChangeEvent.fire(this, token);
  }

  @Override
  public String getToken() {
    return token;
  }

  @Override
  public void newItem(final String historyToken, final boolean issueEvent) {
    token = historyToken;
    if (issueEvent) {
      ValueChangeEvent.fire(this, token);
    }
  }

  @Override
  public void fireEvent(final GwtEvent<?> event) {
    handlers.fireEvent(event);
  }

}
