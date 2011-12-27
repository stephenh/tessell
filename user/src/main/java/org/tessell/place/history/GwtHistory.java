package org.tessell.place.history;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;

public class GwtHistory implements IsHistory {

  @Override
  public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<String> handler) {
    return History.addValueChangeHandler(handler);
  }

  @Override
  public void fireCurrentHistoryState() {
    History.fireCurrentHistoryState();
  }

  @Override
  public String getToken() {
    return History.getToken();
  }

  @Override
  public void newItem(final String historyToken, final boolean issueEvent) {
    History.newItem(historyToken, issueEvent);
  }

}
