package org.tessell.widgets.form.actions;

import org.tessell.model.dsl.Binder;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.FormPresenter;

public interface FormAction {

  void bind(FormPresenter p, Binder binder);

  void renderAction(HTMLPanelBuilder hb);

  void trigger();

}
