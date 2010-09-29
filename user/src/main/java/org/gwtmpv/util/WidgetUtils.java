package org.gwtmpv.util;

import org.gwtmpv.widgets.HasCss;

import com.google.gwt.dom.client.Style.Display;

public class WidgetUtils {

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
