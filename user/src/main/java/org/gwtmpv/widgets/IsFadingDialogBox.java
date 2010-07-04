package org.gwtmpv.widgets;

public interface IsFadingDialogBox extends IsDialogBox {

  void fadeInElement();

  void fadeOutElement();

  boolean isAutoFadeInGlass();

  void setAutoFadeInGlass(boolean autoFadeInGlass);

  boolean isAutoFadeInElement();

  void setAutoFadeInElement(boolean autoFadeInElement);

}
