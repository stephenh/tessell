package org.tessell.widgets.form.lines;

import org.tessell.model.properties.PropertyGroup;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.FormLayout;
import org.tessell.widgets.form.FormPresenter;

/** A line in a form, includes the label, value, and any errors. */
public interface FormLine {

  void bind(FormPresenter p, PropertyGroup all);

  /** Renders this line--can optionally call back into the {@link FormLayout} for default rendering. */
  void render(FormPresenter p, FormLayout l, HTMLPanelBuilder hb);

  void renderLabel(HTMLPanelBuilder hb);

  void renderValue(HTMLPanelBuilder hb);

  void renderErrors(HTMLPanelBuilder hb);

  void focus();

}
