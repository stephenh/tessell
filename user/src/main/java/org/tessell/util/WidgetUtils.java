package org.tessell.util;

import static org.tessell.widgets.Widgets.newAnimation;

import org.tessell.gwt.animation.client.AnimationLogic;
import org.tessell.gwt.animation.client.IsAnimation;
import org.tessell.gwt.user.client.ui.HasCss;
import org.tessell.gwt.user.client.ui.IsFocusWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;

public class WidgetUtils {

  public static IsAnimation fadeOut(final HasCss css) {
    return fadeOut(css, 300);
  }

  public static IsAnimation fadeOut(final HasCss css, int duration) {
    IsAnimation a = newAnimation(new AnimationLogic() {
      @Override
      public void onUpdate(double progress) {
        css.getStyle().setOpacity(1 - progress);
      }

      @Override
      public void onComplete() {
        css.getStyle().setDisplay(Display.NONE);
      }
    });
    a.run(duration);
    return a;
  }

  public static IsAnimation fadeIn(final HasCss css) {
    return fadeIn(css, 300);
  }

  public static IsAnimation fadeIn(final HasCss css, int duration) {
    IsAnimation a = newAnimation(new AnimationLogic() {
      @Override
      public void onStart() {
        css.getStyle().clearDisplay();
        css.getStyle().setOpacity(0.0);
      }

      @Override
      public void onUpdate(double progress) {
        css.getStyle().setOpacity(progress);
      }
    });
    a.run(duration);
    return a;
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

  /** Sets focus on {@code w}, potentially waiting until it's {@link AttachEvent}. */
  public static void focus(final IsFocusWidget w) {
    if (w.isAttached() || !GWT.isClient()) {
      w.setFocus(true);
    } else {
      final HandlerRegistration[] h = new HandlerRegistration[] { null };
      h[0] = w.addAttachHandler(new AttachEvent.Handler() {
        public void onAttachOrDetach(AttachEvent event) {
          if (event.isAttached()) {
            w.setFocus(true);
            if (h[0] != null) {
              h[0].removeHandler();
            }
          }
        }
      });
    }
  }

}
