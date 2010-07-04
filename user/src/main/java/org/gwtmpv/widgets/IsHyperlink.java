package org.gwtmpv.widgets;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasHTML;

public interface IsHyperlink extends IsWidget, HasHTML, HasClickHandlers {

  String getTargetHistoryToken();

  void setTargetHistoryToken(String targetHistoryToken);

}
