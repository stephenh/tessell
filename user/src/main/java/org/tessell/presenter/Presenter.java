package org.tessell.presenter;

import org.tessell.bus.Bound;

import com.google.gwt.user.client.ui.IsWidget;

public interface Presenter extends Bound {

  /** @return The view for the presenter. */
  IsWidget getView();

}
