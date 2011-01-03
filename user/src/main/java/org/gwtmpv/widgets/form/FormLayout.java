package org.gwtmpv.widgets.form;

import org.gwtmpv.util.HTMLPanelBuilder;

public interface FormLayout {

  void render(FormPresenter p, HTMLPanelBuilder hb);

  void formBegin(FormPresenter p, HTMLPanelBuilder hb);

  void formEnd(FormPresenter p, HTMLPanelBuilder hb);

  void lineBegin(FormPresenter p, HTMLPanelBuilder hb);

  void lineEnd(FormPresenter p, HTMLPanelBuilder hb);

  void linesBegin(FormPresenter p, HTMLPanelBuilder hb);

  void linesEnd(FormPresenter p, HTMLPanelBuilder hb);

  void labelBegin(FormPresenter p, HTMLPanelBuilder hb);

  void labelEnd(FormPresenter p, HTMLPanelBuilder hb);

  void valueBegin(FormPresenter p, HTMLPanelBuilder hb);

  void valueEnd(FormPresenter p, HTMLPanelBuilder hb);

  void errorsBegin(FormPresenter p, HTMLPanelBuilder hb);

  void errorsEnd(FormPresenter p, HTMLPanelBuilder hb);

  void actionsBegin(FormPresenter p, HTMLPanelBuilder hb);

  void actionsEnd(FormPresenter p, HTMLPanelBuilder hb);

  void actionBegin(FormPresenter p, HTMLPanelBuilder hb);

  void actionEnd(FormPresenter p, HTMLPanelBuilder hb);

}
