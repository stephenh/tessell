package org.gwtmpv.widgets.form.lines;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.util.HTMLPanelBuilder;

public interface FormLine {

  void bind(String formId, Binder binder);

  void renderLabel(HTMLPanelBuilder hb);

  void renderValue(HTMLPanelBuilder hb);

}
