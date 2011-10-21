package org.gwtmpv.widgets.form.lines;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.form.FormLayout;
import org.gwtmpv.widgets.form.FormPresenter;

/** A line in a form, includes the label, value, and any errors. */
public interface FormLine {

  void bind(FormPresenter p, PropertyGroup all, Binder binder);

  /** Renders this line--can optionally call back into the {@link FormLayout} for default rendering. */
  void render(FormPresenter p, FormLayout l, HTMLPanelBuilder hb);

  void renderLabel(HTMLPanelBuilder hb);

  void renderValue(HTMLPanelBuilder hb);

  void renderErrors(HTMLPanelBuilder hb);

  void focus();

}
