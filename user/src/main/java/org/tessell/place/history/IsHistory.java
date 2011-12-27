package org.tessell.place.history;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

/**
 * An interface to replace static calls to {@link com.google.gwt.user.client.History}.
 * 
 * See {@link GwtHistory} and {@link StubHistory}.
 */
public interface IsHistory {

  HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler);

  String getToken();

  void newItem(String historyToken, boolean issueEvent);

  void fireCurrentHistoryState();

}
