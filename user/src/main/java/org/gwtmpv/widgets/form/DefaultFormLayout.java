package org.gwtmpv.widgets.form;

import org.gwtmpv.util.HTMLPanelBuilder;

public class DefaultFormLayout implements FormLayout {

  @Override
  public void formBegin(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("<div>");
  }

  @Override
  public void formEnd(FormPresenter p, HTMLPanelBuilder hb) {
    hb.add("</div>");
  }

}
