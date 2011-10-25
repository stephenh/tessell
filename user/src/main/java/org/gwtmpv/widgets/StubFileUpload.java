package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class StubFileUpload extends StubWidget implements IsFileUpload {

  private String filename;
  private boolean enabled;
  private String name;

  public void setFilename(String filename) {
    this.filename = filename;
    fireEvent(new DummyChange());
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return handlers.addHandler(ChangeEvent.getType(), handler);
  }

  @Override
  public String getFilename() {
    return filename;
  }

}
