package org.tessell.gwt.user.client.ui;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.tessell.util.Stubs;

public class StubDragLogicTest {

  @Before
  public void resetStubs() {
    Stubs.reset();
  }

  @Test
  public void elementMustBeDraggable() {
    StubFocusWidget w = new StubFocusWidget();
    try {
      w.dragStart();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Element must have draggable=true"));
    }

    w.getIsElement().setAttribute("draggable", "true");
    w.dragStart();
  }

  @Test
  public void anchorsAreAlreadyDraggable() {
    StubAnchor a = new StubAnchor();
    a.dragStart();
  }

  @Test
  public void anchorsCanBeMarkedNotDraggable() {
    StubAnchor a = new StubAnchor();
    a.getIsElement().setAttribute("draggable", "false");
    try {
      a.dragStart();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Element must have draggable=true"));
    }
  }

  @Test
  public void firesDragEndOnPreviousDraggable() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    boolean[] stopped = { false };
    a1.addDragEndHandler(e -> stopped[0] = true);
    a1.dragStart();
    a2.dragStart();
    assertThat(stopped[0], is(true));
  }

  @Test
  public void firefoxRequiresSetDataToBeCalled() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> {
    });
    a1.dragStart();
    try {
      a2.dragOver();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("Firefox requires setData to be called in drag start"));
    }
  }

  @Test
  public void mustHaveDragOverToAllowDrop() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a1.dragStart();
    try {
      a2.drop();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("addDragOverHandler must be called to get drop events"));
    }
  }

  @Test
  public void dropRequiresDragStart() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a2.addDragOverHandler(e -> {
    });
    try {
      a2.drop();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("No stub dragStart() was called"));
    }
  }

  @Test
  public void dropRequiresLastDragOverToBeCanceled() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a2.addDragOverHandler(e -> {
    });
    a1.dragStart();
    a2.dragOver();
    try {
      a2.drop();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("addDragOverHandler should call preventDefault() if it wants to drop"));
    }
  }

  @Test
  public void dropHandlerShouldPreventDefault() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a2.addDragOverHandler(e -> {
      e.preventDefault();
    });
    a1.dragStart();
    a2.dragOver();
    try {
      a2.drop();
      fail();
    } catch (IllegalStateException ise) {
      assertThat(ise.getMessage(), is("addDropHandler should call preventDefault()"));
    }
  }

  @Test
  public void successfulDragAndDrop() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a2.addDragOverHandler(e -> {
      e.preventDefault();
    });
    a2.addDropHandler(e -> {
      e.preventDefault();
    });
    a1.dragStart();
    a2.dragOver();
    a2.drop();
  }

  @Test
  public void dropImplicitlyCallsOverIfNotExplicitlyCalled() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a2.addDragOverHandler(e -> {
      e.preventDefault();
    });
    a2.addDropHandler(e -> {
      e.preventDefault();
    });
    a1.dragStart();
    // no explicit dragOver
    // a2.dragOver();
    a2.drop();
  }

  @Test
  public void dropCallsDragEnd() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    boolean[] ended = { false };
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a1.addDragEndHandler(e -> ended[0] = true);
    a2.addDragOverHandler(e -> {
      e.preventDefault();
    });
    a2.addDropHandler(e -> {
      e.preventDefault();
    });
    a1.dragStart();
    a2.drop();
    assertThat(ended[0], is(true));
  }

  @Test
  public void dataIsPassedToDrop() {
    StubAnchor a1 = new StubAnchor();
    StubAnchor a2 = new StubAnchor();
    boolean[] ended = { false };
    a1.addDragStartHandler(e -> e.setData("text/plain", "foo"));
    a1.addDragEndHandler(e -> ended[0] = true);
    a2.addDragOverHandler(e -> {
      e.preventDefault();
    });
    a2.addDropHandler(e -> {
      assertThat(e.getData("text/plain"), is("foo"));
      e.preventDefault();
    });
    a1.dragStart();
    a2.drop();
  }
}
