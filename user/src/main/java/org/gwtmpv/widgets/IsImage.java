package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasErrorHandlers;
import com.google.gwt.event.dom.client.HasLoadHandlers;

public interface IsImage extends IsWidget, HasLoadHandlers, HasErrorHandlers, HasClickHandlers, HasAllMouseHandlers {

  String getUrl();

  void setUrl(String url);

}
