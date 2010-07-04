package org.gwtmpv.widgets;

public class StubFadingDialogBox extends StubDialogBox implements IsFadingDialogBox {

  private boolean autoFadeInGlass;
  private boolean autoFadeInElement;
  private boolean glassFadedIn;
  private boolean elementFadedIn;

  @Override
  public void show() {
    super.show();
    if (isAnimationEnabled() && autoFadeInGlass) {
      glassFadedIn = true;
    }
    if (isAnimationEnabled() && autoFadeInElement) {
      elementFadedIn = true;
    }
  }

  @Override
  public boolean isAutoFadeInGlass() {
    return autoFadeInGlass;
  }

  @Override
  public void setAutoFadeInGlass(final boolean autoFadeInGlass) {
    this.autoFadeInGlass = autoFadeInGlass;
  }

  @Override
  public boolean isAutoFadeInElement() {
    return autoFadeInElement;
  }

  @Override
  public void setAutoFadeInElement(final boolean autoFadeInElement) {
    this.autoFadeInElement = autoFadeInElement;
  }

  @Override
  public void fadeInElement() {
    elementFadedIn = true;
  }

  /** @return whether the glass has been auto faded in, for assertions */
  public boolean isGlassFadedIn() {
    return glassFadedIn;
  }

  /** @return whether the element has been auto or manually faded in, for assertions */
  public boolean isElementFadedIn() {
    return elementFadedIn;
  }

  @Override
  public void fadeOutElement() {
    elementFadedIn = false;
  }

}
