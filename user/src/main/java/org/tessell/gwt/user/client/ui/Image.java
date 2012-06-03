package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.OtherTypes;

import com.google.gwt.resources.client.ImageResource;

@OtherTypes(intf = IsImage.class, stub = StubImage.class)
public class Image extends com.google.gwt.user.client.ui.Image implements IsImage {

  public Image() {
  }

  public Image(ImageResource resource) {
    super(resource);
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

}
