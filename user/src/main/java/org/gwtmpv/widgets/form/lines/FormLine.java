package org.gwtmpv.widgets.form.lines;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.form.FormPresenter;

public interface FormLine {

  void bind(FormPresenter p, PropertyGroup all, Binder binder);

  void renderLabel(HTMLPanelBuilder hb);

  void renderValue(HTMLPanelBuilder hb);

  void renderErrors(HTMLPanelBuilder hb);

  void focus();

}
