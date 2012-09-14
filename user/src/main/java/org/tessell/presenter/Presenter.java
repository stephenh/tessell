package org.tessell.presenter;

import org.tessell.bus.Bound;
import org.tessell.gwt.user.client.ui.IsWidget;

public interface Presenter extends Bound {

  /** @return The view for the presenter. */
  IsWidget getView();

}
