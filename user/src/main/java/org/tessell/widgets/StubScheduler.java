package org.tessell.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.user.cellview.client.PublicHasDataPresenter;

/** A fake scheduler that can as-needed pretend to defer commands or execute them immediately. */
public class StubScheduler extends SchedulerImpl {

  private int deferredLevel = 0;
  private final List<ScheduledCommand> deferred = new ArrayList<ScheduledCommand>();
  private final List<RepeatingCommand> repeating = new ArrayList<RepeatingCommand>();

  public static StubScheduler get() {
    return StubGWTBridge.getScheduler();
  }

  /** An ugly way to temporarily defer executing commands.
   * 
   * Currently used for {@link StubCellTable}, whose {@link PublicHasDataPresenter} very
   * strictly assumes that {@link scheduledFinally} commands will not execute right away.
   */
  public void runWithDeferred(Runnable run) {
    deferredLevel++;
    run.run();

    // if the users nests runWithDeferred calls, e.g. uses one in a test while
    // we also add one in StubCellTable, don't execute the deferred commands
    // until the last one is complete
    deferredLevel--;
    if (deferredLevel == 0) {
      for (Iterator<ScheduledCommand> i = deferred.iterator(); i.hasNext();) {
        i.next().execute();
        i.remove();
      }

      for (Iterator<RepeatingCommand> i = repeating.iterator(); i.hasNext();) {
        i.next().execute();
        i.remove();
      }
    }
  }

  @Override
  public void scheduleDeferred(final ScheduledCommand cmd) {
    if (executeImmediately()) {
      cmd.execute();
    } else {
      deferred.add(cmd);
    }
  }

  @Override
  public void scheduleEntry(final RepeatingCommand cmd) {
  }

  @Override
  public void scheduleEntry(final ScheduledCommand cmd) {
  }

  @Override
  public void scheduleFinally(final RepeatingCommand cmd) {
    if (executeImmediately()) {
      cmd.execute();
    } else {
      repeating.add(cmd);
    }
  }

  @Override
  public void scheduleFinally(final ScheduledCommand cmd) {
    if (executeImmediately()) {
      cmd.execute();
    } else {
      deferred.add(cmd);
    }
  }

  @Override
  public void scheduleFixedDelay(final RepeatingCommand cmd, final int delayMs) {
  }

  @Override
  public void scheduleFixedPeriod(final RepeatingCommand cmd, final int delayMs) {
  }

  @Override
  public void scheduleIncremental(final RepeatingCommand cmd) {
  }

  private boolean executeImmediately() {
    return deferredLevel == 0;
  }

}
