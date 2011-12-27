package org.tessell.presenter;


/** Eagerly binds/unbinds a presenter for our parent. */
public class Slot<C extends Presenter> {

  private final BasicPresenter<?> parent;
  private C current;

  public Slot(final BasicPresenter<?> parent) {
    this.parent = parent;
  }

  public void set(final C presenter) {
    if (current == presenter) {
      return;
    }
    if (current != null) {
      parent.removePresenter(current);
    }
    current = presenter;
    parent.addPresenter(current);
  }

  public C get() {
    return current;
  }

}
