package org.gwtmpv.widgets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class StubAnimationTest {

  private final DummyLogic l = new DummyLogic();

  @Test
  public void autoFinishes() {
    StubAnimation a = new StubAnimation(l);
    a.run(100);
    assertThat(l.progresses, contains(0.0, 1.0));
  }

  @Test
  public void cannotTickAfterAutoFinishes() {
    StubAnimation a = new StubAnimation(l);
    a.run(100);
    assertThat(l.progresses, contains(0.0, 1.0));
    try {
      a.tick(1.0);
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Animation is not currently in progress"));
    }
  }

  @Test
  public void cannotTickWhenUnscheduled() {
    StubAnimation a = new StubAnimation(l);
    try {
      a.tick(1.0);
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Animation is not currently in progress"));
    }
  }

  @Test
  public void manualTicking() {
    StubAnimation a = new StubAnimation(l);
    a.doNotAutoFinish();
    a.run(200);
    a.tick(0.0);
    assertThat(l.started, is(true));
    a.tick(0.5);
    assertThat(l.complete, is(false));
    a.tick(1.0);
    assertThat(l.complete, is(true));
    assertThat(l.progresses, contains(0.0, 0.5, 1.0));
  }

  private static class DummyLogic extends AnimationLogic {
    private final List<Double> progresses = new ArrayList<Double>();
    private boolean complete;
    private boolean started;

    @Override
    public void onUpdate(double progress) {
      progresses.add(progress);
    }

    @Override
    public void onStart() {
      started = true;
      super.onStart();
    }

    @Override
    public void onComplete() {
      complete = true;
      super.onComplete();
    }

    @Override
    public double interpolate(double progress) {
      return progress; // easier testing
    }
  }

}
