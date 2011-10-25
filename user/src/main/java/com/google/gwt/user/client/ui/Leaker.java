package com.google.gwt.user.client.ui;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class Leaker {

  public static Type<SubmitHandler> getSubmitEventType() {
    return SubmitEvent.getType();
  }

  public static Type<SubmitCompleteHandler> getSubmitCompleteEventType() {
    return SubmitCompleteEvent.getType();
  }

}
