package org.tessell.util;

import static org.tessell.widgets.Widgets.newAnimation;

import java.util.ArrayList;

import org.tessell.gwt.animation.client.AnimationLogic;
import org.tessell.gwt.animation.client.IsAnimation;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.ui.IsWidget;

import com.google.gwt.dom.client.Style.Display;

/** JQuery-style effects that work against an {@link IsElement}. */
public class Effects {

  public static Effects effects(IsWidget w) {
    return effects(w.getIsElement());
  }

  public static Effects effects(IsElement e) {
    return new Effects(e);
  }

  private final IsElement e;
  private final ArrayList<QueuedEffect> effects = new ArrayList<QueuedEffect>();
  private boolean running;

  private Effects(IsElement e) {
    this.e = e;
  }

  public Effects delay(int millis) {
    effects.add(new QueuedEffect(new AnimationLogic() {
      public void onUpdate(double progress) {
        // do nothing
      }
    }, millis, null));
    runNextIfNeeded();
    return this;
  }

  public Effects fadeIn() {
    effects.add(new QueuedEffect(new AnimationLogic() {
      @Override
      public void onUpdate(double progress) {
        e.getStyle().setOpacity(progress);
      }

      @Override
      public void onStart() {
        super.onStart();
        e.getStyle().clearDisplay();
      }
    }, 350, null));
    runNextIfNeeded();
    return this;
  }

  public Effects fadeOut() {
    effects.add(new QueuedEffect(new AnimationLogic() {
      @Override
      public void onUpdate(double progress) {
        e.getStyle().setOpacity(1 - progress);
      }
    }, 350, null));
    runNextIfNeeded();
    return this;
  }

  public Effects drop() {
    effects.add(new QueuedEffect(new AnimationLogic() {
      public void onUpdate(double progress) {
        // do nothing
      }

      public void onComplete() {
        e.removeFromParent();
      }
    }, 0, null));
    runNextIfNeeded();
    return this;
  }

  public Effects hide() {
    effects.add(new QueuedEffect(new AnimationLogic() {
      public void onUpdate(double progress) {
        // do nothing
      }

      public void onComplete() {
        e.getStyle().setDisplay(Display.NONE);
      }
    }, 0, null));
    runNextIfNeeded();
    return this;
  }

  private void runNextIfNeeded() {
    if (running || effects.size() == 0) {
      return;
    }
    final QueuedEffect next = effects.remove(0);
    IsAnimation wrapper = newAnimation(new AnimationLogic() {
      @Override
      public void onStart() {
        next.logic.onStart();
      }

      @Override
      public void onUpdate(double progress) {
        next.logic.onUpdate(progress);
      }

      @Override
      public void onComplete() {
        next.logic.onComplete();
        if (next.callback != null) {
          next.callback.run();
        }
        running = false;
        runNextIfNeeded();
      }

      @Override
      public double interpolate(double progress) {
        return next.logic.interpolate(progress);
      }
    });
    running = true; // set running first in case duration=0
    wrapper.run(next.duration);
  }

  /** DTO to store a pending animation in the queue. */
  private static class QueuedEffect {
    private final AnimationLogic logic;
    private final int duration;
    private final Runnable callback;

    private QueuedEffect(AnimationLogic logic, int duration, Runnable callback) {
      this.logic = logic;
      this.duration = duration;
      this.callback = callback;
    }
  }

}
