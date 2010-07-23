package org.gwtmpv.presenter.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.presenter.Presenter;

@GenEvent
public class PresenterChangedEventSpec {
  @Param(1)
  Presenter presenter;
}
