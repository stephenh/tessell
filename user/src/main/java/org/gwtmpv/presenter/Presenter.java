package org.gwtmpv.presenter;

import org.gwtmpv.bus.Bound;
import org.gwtmpv.widgets.IsWidget;

public interface Presenter extends Bound {

  /** @return The view for the presenter. */
  IsWidget getView();

  /** Requests the presenter to reveal the view on screen. */
  void revealDisplay();

}
