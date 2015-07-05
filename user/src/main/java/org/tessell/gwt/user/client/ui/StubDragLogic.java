package org.tessell.gwt.user.client.ui;

import org.tessell.gwt.dom.client.*;
import org.tessell.util.Stubs;
import org.tessell.widgets.StubWidget;

import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;

/**
 * Attempts to mimick drag/drag behavior just enough for us to
 * reliably unit test behavior in presenter tests.
 *
 * Note that some browsers are pickier than others; e.g. Chrome
 * seems fairly lenient, and does not care if the user does not
 * call "setData" or "preventDefault" in dragstart. Firefox does.
 *
 * This attempts to match the most-strict behavior, so that unit
 * tests have the best chance of ensuring success in actual browsers.
 */
class StubDragLogic {

  public static HasAllDragAndDropHandlers currentDraggable;
  public static StubDragStartEvent currentStart;
  public static StubDragOverEvent currentOver;
  public static HasAllDragAndDropHandlers currentOverOwner;
  public static StubDropEvent currentDrop;
  private final HasAllDragAndDropHandlers owner;
  private boolean hasDragOver;

  private static void reset() {
    currentDraggable = null;
    currentStart = null;
    currentOver = null;
    currentOverOwner = null;
    currentDrop = null;
  }

  static {
    Stubs.addAfterTestReset(() -> reset());
  }

  StubDragLogic(HasAllDragAndDropHandlers owner) {
    this.owner = owner;
  }

  void markHasDragOverHandler() {
    hasDragOver = true;
  }

  void dragStart() {
    ensureDraggable();
    if (currentStart != null) {
      currentDraggable.fireEvent(new StubDragEndEvent());
    }
    currentDraggable = owner;
    currentStart = new StubDragStartEvent();
    owner.fireEvent(currentStart);
  }

  void dragEnd() {
    owner.fireEvent(new StubDragEndEvent());
  }

  void dragEnter() {
    owner.fireEvent(new StubDragEnterEvent());
  }

  void dragLeave() {
    owner.fireEvent(new StubDragLeaveEvent());
  }

  void dragOver() {
    ensureCanBeDraggedOver();
    currentOver = new StubDragOverEvent();
    currentOverOwner = owner;
    owner.fireEvent(currentOver);
  }

  void drop() {
    // implicitly dragOver so that unit tests can be less verbose
    if (currentOverOwner != owner) {
      dragOver();
    }
    ensureCanBeDroppedOn();
    currentDrop = new StubDropEvent(currentStart.mimeType, currentStart.data);
    owner.fireEvent(currentDrop);
    if (!currentDrop.prevented) {
      throw new IllegalStateException("addDropHandler should call preventDefault()");
    }
    currentDraggable.fireEvent(new StubDragEndEvent());
    reset();
  }

  private void ensureCanBeDraggedOver() {
    if (currentDraggable == null) {
      throw new IllegalStateException("No stub dragStart() was called");
    }
    if (currentStart.mimeType == null && currentStart.data == null) {
      throw new IllegalStateException("Firefox requires setData to be called in drag start");
    }
  }

  private void ensureCanBeDroppedOn() {
    if (currentDraggable == null) {
      throw new IllegalStateException("No stub dragStart() was called");
    }
    if (currentStart.mimeType == null && currentStart.data == null) {
      throw new IllegalStateException("Firefox requires setData to be called in drag start");
    }
    if (!hasDragOver) {
      throw new IllegalStateException("addDragOverHandler must be called to get drop events");
    }
    if (!currentOver.prevented) {
      throw new IllegalStateException("addDragOverHandler should call preventDefault() if it wants to drop");
    }
  }

  private void ensureDraggable() {
    String draggable = ((StubWidget) owner).getIsElement().getAttribute("draggable");
    if ("false".equals(draggable)) {
      throw new IllegalStateException("Element must have draggable=true");
    }
    // these are draggable by default
    if (owner instanceof IsAnchor || owner instanceof IsImage) {
      return;
    }
    if (!"true".equals(draggable)) {
      throw new IllegalStateException("Element must have draggable=true");
    }
  }
}
