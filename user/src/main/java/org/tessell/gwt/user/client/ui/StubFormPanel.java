package org.tessell.gwt.user.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.*;

public class StubFormPanel extends StubSimplePanel implements IsFormPanel {

  private String action;
  private String encoding;
  private String method;
  private String target;
  private boolean submitCanceled;
  private boolean submitting;

  public void submitComplete(String serverResponse) {
    if (!submitting) {
      throw new IllegalStateException("Form was not submitting");
    }
    submitting = false;
    fireEvent(new SubmitCompleteEvent(serverResponse) {
    });
  }

  public boolean wasSubmitCanceled() {
    return submitCanceled;
  }

  public boolean isSubmitting() {
    return submitting;
  }

  @Override
  public void submit() {
    SubmitEvent event = new SubmitEvent();
    fireEvent(event);
    submitCanceled = event.isCanceled();
    if (!submitCanceled) {
      submitting = true;
    }
  }

  @Override
  public HandlerRegistration addSubmitCompleteHandler(SubmitCompleteHandler handler) {
    return handlers.addHandler(Leaker.getSubmitCompleteEventType(), handler);
  }

  @Override
  public HandlerRegistration addSubmitHandler(SubmitHandler handler) {
    return handlers.addHandler(Leaker.getSubmitEventType(), handler);
  }

  @Override
  public String getAction() {
    return action;
  }

  @Override
  public String getEncoding() {
    // TODO Auto-generated method stub
    return encoding;
  }

  @Override
  public String getMethod() {
    return method;
  }

  @Override
  public String getTarget() {
    return target;
  }

  @Override
  public void reset() {
  }

  @Override
  public void setAction(String action) {
    this.action = action;
  }

  @Override
  public void setAction(SafeUri url) {
    action = url.asString();
  }

  @Override
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  @Override
  public void setMethod(String method) {
    this.method = method;
  }

}
