package org.gwtmpv.tests.bus;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.gwtmpv.bus.DefaultEventBus;
import org.gwtmpv.bus.EventBus;
import org.gwtmpv.place.DefaultPlaceManager;
import org.gwtmpv.place.Place;
import org.gwtmpv.place.PlaceManager;
import org.gwtmpv.place.PlaceRequest;
import org.gwtmpv.place.PresenterPlace;
import org.gwtmpv.place.history.StubHistory;
import org.gwtmpv.place.tokenizer.DefaultTokenizer;
import org.gwtmpv.presenter.BasicPresenter;
import org.gwtmpv.presenter.Presenter;
import org.gwtmpv.widgets.IsWidget;
import org.junit.Test;

public class BusTest {

  private final EventBus testBus = new DefaultEventBus();

  @Test
  public void addsAreNotDeferred() {
    final Presenter child = new ChildPresenter(null, testBus);
    final ParentPresenter parent = new ParentPresenter(null, testBus, child);

    final Place p = new ParentPlace(testBus, "parent", parent);
    final PlaceManager placeManager = new DefaultPlaceManager(testBus, new DefaultTokenizer(), new StubHistory());
    placeManager.registerPlace(p);

    testBus.fireEvent(new PlaceRequest("parent").asEvent());

    assertThat(parent.childRevealedCalled, is(true));
  }

  private final class ParentPlace extends PresenterPlace<ParentPresenter> {
    private final ParentPresenter parent;

    private ParentPlace(final EventBus eventBus, final String name, final ParentPresenter parent) {
      super(eventBus, name);
      this.parent = parent;
    }

    @Override
    public void handleRequest(final PlaceRequest request) {
      currentPresenter = parent;
      parent.bind();
      parent.revealDisplay();
    }
  }

  private final class ParentPresenter extends BasicPresenter<IsWidget> {
    private final Presenter child;
    private boolean childRevealedCalled = false;

    private ParentPresenter(final IsWidget isWidget, final EventBus eventBus, final Presenter child) {
      super(isWidget, eventBus);
      this.child = child;
    }

    @Override
    public void onRevealDisplay() {
      super.onRevealDisplay();
      addPresenter(child).revealDisplay();
    }

    @Override
    protected void onChildRevealed(final Presenter presenter) {
      super.onChildRevealed(presenter);
      childRevealedCalled = true;
    }
  }

  private final class ChildPresenter extends BasicPresenter<IsWidget> {
    private ChildPresenter(final IsWidget isWidget, final EventBus eventBus) {
      super(isWidget, eventBus);
    }
  }
}
