package org.gwtmpv.widgets.form.actions;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.form.FormPresenter;

public interface FormAction {

  void bind(FormPresenter p, Binder binder);

  void renderAction(HTMLPanelBuilder hb);

  void trigger();

}
