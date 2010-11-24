package org.gwtmpv.widgets.form.lines;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;

public interface FormLine {

  void bind(String formId, PropertyGroup all, Binder binder);

  void renderLabel(HTMLPanelBuilder hb);

  void renderValue(HTMLPanelBuilder hb);

  void renderErrors(HTMLPanelBuilder hb);

}
