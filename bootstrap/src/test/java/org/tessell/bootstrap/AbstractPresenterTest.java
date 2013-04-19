package org.tessell.bootstrap;

import org.tessell.bootstrap.resources.BootstrapStyle;
import org.tessell.bootstrap.resources.StubAppResources;
import org.tessell.bootstrap.views.StubViewsProvider;
import org.tessell.widgets.StubGWTBridge;
import org.tessell.widgets.StubWidgetsProvider;

public abstract class AbstractPresenterTest {

  protected final StubAppResources resources = new StubAppResources();
  protected final BootstrapStyle b = resources.bootstrap();

  protected AbstractPresenterTest() {
    StubGWTBridge.install();
    StubWidgetsProvider.install();
    StubViewsProvider.install(b);
  }

}
