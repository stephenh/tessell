package org.tessell.widgets;

import com.google.gwt.user.client.ui.FileUpload;

public class GwtFileUpload extends FileUpload implements IsFileUpload {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
