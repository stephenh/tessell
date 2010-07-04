package org.gwtmpv.tests.presenter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.gwtmpv.bus.DefaultEventBus;
import org.gwtmpv.bus.EventBus;
import org.gwtmpv.presenter.BasicPresenter;
import org.gwtmpv.presenter.Presenter;
import org.gwtmpv.presenter.Slot;
import org.gwtmpv.widgets.IsWidget;
import org.junit.Test;

public class SlotTest {

  protected final EventBus testBus = new DefaultEventBus();

  @Test
  public void doubleDisplayDoesNotUnbind() {
    final ChildPresenter child = new ChildPresenter();
    final ParentPresenter p = new ParentPresenter(child);
    assertThat(child.wasBound, is(false));

    // Just binding does not set current
    p.bind();
    assertThat(child.wasBound, is(true));
    assertThat(p.getCurrent(), is((Presenter) null));

    // child.revealDisplay();
    p.set(child);
    assertThat(child.wasBound, is(true));
    assertThat(child.wasUnbound, is(false));
    assertThat(p.getCurrent(), is((Presenter) child));

    // 2nd time we're still current
    // child.revealDisplay();
    p.set(child);
    assertThat(child.wasBound, is(true));
    assertThat(child.wasUnbound, is(false));
    assertThat(p.getCurrent(), is((Presenter) child));
  }

  @Test
  public void twoChildren() {
    final ChildPresenter c1 = new ChildPresenter();
    final ChildPresenter c2 = new ChildPresenter();
    final ChildPresenter c1b = new ChildPresenter();
    final ParentPresenter p = new ParentPresenter(c1, c2);

    p.bind();
    assertThat(p.getCurrent(), is((Presenter) null));

    // c1.revealDisplay();
    p.set(c1);
    assertThat(p.getCurrent(), is((Presenter) c1));

    // c2.revealDisplay();
    p.set(c2);
    assertThat(p.getCurrent(), is((Presenter) c2));

    // cannot reuse c1
    try {
      p.set(c1);
      fail();
    } catch (final IllegalStateException ise) {
      assertThat(ise.getMessage().startsWith("This instance has already been unbound"), is(true));
    }

    // c1.revealDisplay();
    p.set(c1b);
    assertThat(p.getCurrent(), is((Presenter) c1b));
  }

  public class ParentPresenter extends BasicPresenter<IsWidget> {
    private final Slot<Presenter> current = new Slot<Presenter>(this);

    public ParentPresenter(final Presenter... children) {
      super(null, testBus);
      for (final Presenter child : children) {
        addPresenter(child);
      }
    }

    public void set(final Presenter presenter) {
      current.set(presenter);
    }

    public Presenter getCurrent() {
      return current.get();
    }
  }

  public class ChildPresenter extends BasicPresenter<IsWidget> {
    public boolean wasBound = false;
    public boolean wasUnbound = false;

    public ChildPresenter() {
      super(null, testBus);
    }

    @Override
    public void onBind() {
      super.onBind();
      wasBound = true;
    }

    @Override
    public void onUnbind() {
      super.onUnbind();
      wasUnbound = true;
    }
  }
}
