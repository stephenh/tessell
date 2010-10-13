package org.gwtmpv.widgets.form;

import org.gwtmpv.util.HTMLPanelBuilder;

public interface FormLayout {

  void formBegin(FormPresenter p, HTMLPanelBuilder hb);

  void formEnd(FormPresenter p, HTMLPanelBuilder hb);

}
