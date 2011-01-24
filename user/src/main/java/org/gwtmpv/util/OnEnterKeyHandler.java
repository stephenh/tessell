package org.gwtmpv.util;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_ENTER;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

/** Key events are a bitch.
 *
 * We want to trigger on key up, as that comes after change, so the model
 * will be up to date. However, if "enter" is pressed somewhere else, and
 * focus is moved to us, we'll catch the tail end of the key up. So watch
 * for key down/up pairs that match.
 */
public class OnEnterKeyHandler implements KeyUpHandler, KeyDownHandler {

  private final Runnable runnable;
  private int lastKey;

  public OnEnterKeyHandler(Runnable runnable) {
    this.runnable = runnable;
  }

  @Override
  public void onKeyDown(KeyDownEvent event) {
    lastKey = event.getNativeKeyCode();
  }

  @Override
  public void onKeyUp(KeyUpEvent event) {
    if (event.getNativeKeyCode() == lastKey && event.getNativeKeyCode() == KEY_ENTER) {
      runnable.run();
    }
  }

}
