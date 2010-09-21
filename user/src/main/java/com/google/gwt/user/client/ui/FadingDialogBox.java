package com.google.gwt.user.client.ui;

import com.google.gwt.animation.client.Animation;

/** Fades in a dialog box instead of using clip. */
public class FadingDialogBox extends DialogBox {

  private boolean autoFadeInElement;
  private Animation a;

  public FadingDialogBox() {
    super(true, true);
    setAnimation(new FadingAnimation());
  }

  /** Fades in the element after the glass is already showing; call when the content is ready. */
  public void fadeInElement() {
    if (a != null) {
      a.cancel();
    }
    a = new Animation() {
      protected void onUpdate(final double progress) {
        getElement().getStyle().setOpacity(progress);
      }
    };
    a.run(200); // ANIMATION_DURATION is private
  }

  public void fadeOutElement() {
    if (a != null) {
      a.cancel();
    }
    a = new Animation() {
      protected void onUpdate(final double progress) {
        getElement().getStyle().setOpacity(1 - progress);
      }
    };
    a.run(200); // ANIMATION_DURATION is private
  }

  /** A custom {@link ResizeAnimation} that uses opacity. */
  private class FadingAnimation extends ResizeAnimation {
    // The super showing field is private, so we need our own
    private boolean showing = false;

    private FadingAnimation() {
      super(FadingDialogBox.this);
    }

    @Override
    public void setState(final boolean showing, boolean isUnloading) {
      // Override merely to track our own showing variable
      this.showing = showing;
      super.setState(showing, isUnloading);
    }

    @Override
    protected void onStart() {
      if (showing) {
        getElement().getStyle().setOpacity(0);
        getGlassElement().getStyle().setOpacity(0);
      }
    }

    @Override
    protected void onUpdate(double progress) {
      if (!showing) {
        progress = 1.0 - progress;
      }
      getGlassElement().getStyle().setOpacity(progress);
      if (isAutoFadeInElement() || !showing) {
        getElement().getStyle().setOpacity(progress);
      }
    }

    @Override
    protected void onComplete() {
      if (showing) {
        getElement().getStyle().setOpacity(1);
        getGlassElement().getStyle().setOpacity(1);
      }
      super.onComplete();
    }

  }

  public boolean isAutoFadeInElement() {
    return autoFadeInElement;
  }

  public void setAutoFadeInElement(final boolean autoFadeInElement) {
    this.autoFadeInElement = autoFadeInElement;
  }

}
