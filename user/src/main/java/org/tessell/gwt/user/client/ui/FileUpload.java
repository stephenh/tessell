package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.OtherTypes;

@OtherTypes(intf = IsFileUpload.class, stub = StubFileUpload.class)
public class FileUpload extends com.google.gwt.user.client.ui.FileUpload implements IsFileUpload {

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

}
