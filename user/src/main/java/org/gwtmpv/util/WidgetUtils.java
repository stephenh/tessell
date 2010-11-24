package org.gwtmpv.util;

import org.gwtmpv.widgets.HasCss;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;

public class WidgetUtils {

  public static void fadeOut(final HasCss css) {
    fadeOut(css, 300);
  }

  public static void fadeOut(final HasCss css, int duration) {
    if (GWT.isClient()) {
      new Animation() {
        @Override
        protected void onUpdate(double progress) {
          css.getStyle().setOpacity(interpolate(1 - progress));
        }

        @Override
        protected void onComplete() {
          css.getStyle().setDisplay(Display.NONE);
        }
      }.run(duration);
    } else {
      css.getStyle().setOpacity(0.0);
      css.getStyle().setDisplay(Display.NONE);
    }
  }

  public static void fadeIn(final HasCss css) {
    fadeIn(css, 300);
  }

  public static void fadeIn(final HasCss css, int duration) {
    if (GWT.isClient()) {
      new Animation() {
        @Override
        protected void onUpdate(double progress) {
          css.getStyle().setOpacity(interpolate(progress));
        }

        @Override
        protected void onStart() {
          css.getStyle().clearDisplay();
          css.getStyle().setOpacity(0.0);
        }
      }.run(duration);
    } else {
      css.getStyle().setOpacity(1.0);
      css.getStyle().clearDisplay();
    }
  }

  /** Sets {@code display=none} on each element. */
  public static void hide(HasCss... csses) {
    for (HasCss css : csses) {
      css.getStyle().setDisplay(Display.NONE);
    }
  }

  /** Clears {@code display} on each element. */
  public static void show(HasCss... csses) {
    for (HasCss css : csses) {
      css.getStyle().clearDisplay();
    }
  }

}
