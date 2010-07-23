package org.gwtmpv.presenter.events;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.presenter.Presenter;

@GenEvent
public class PresenterUnboundEventSpec {
  @Param(1)
  Presenter presenter;
}
