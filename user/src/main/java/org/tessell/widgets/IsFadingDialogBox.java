package org.tessell.widgets;

public interface IsFadingDialogBox extends IsDialogBox {

  void fadeInElement();

  void fadeOutElement();

  boolean isAutoFadeInElement();

  void setAutoFadeInElement(boolean autoFadeInElement);

}
