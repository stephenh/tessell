package org.tessell.widgets.form.actions;

import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.FormPresenter;

public interface FormAction {

  void bind(FormPresenter p);

  void renderAction(HTMLPanelBuilder hb);

  void trigger();

}
