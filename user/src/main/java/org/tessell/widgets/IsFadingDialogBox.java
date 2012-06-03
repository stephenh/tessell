package org.tessell.widgets;

import org.tessell.gwt.user.client.ui.IsDialogBox;

public interface IsFadingDialogBox extends IsDialogBox {

  void fadeInElement();

  void fadeOutElement();

  boolean isAutoFadeInElement();

  void setAutoFadeInElement(boolean autoFadeInElement);

}
