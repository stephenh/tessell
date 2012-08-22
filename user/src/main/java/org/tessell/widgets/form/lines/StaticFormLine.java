package org.tessell.widgets.form.lines;

import org.tessell.model.dsl.Binder;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.FormPresenter;

/** A base class for form lines that have completely custom, most likely static render methods. */
public abstract class StaticFormLine implements FormLine {

  @Override
  public void bind(FormPresenter p, PropertyGroup all, Binder binder) {
  }

  @Override
  public void renderLabel(HTMLPanelBuilder hb) {
    throw new IllegalStateException("Expected render to be overriden");
  }

  @Override
  public void renderValue(HTMLPanelBuilder hb) {
    throw new IllegalStateException("Expected render to be overriden");
  }

  @Override
  public void renderErrors(HTMLPanelBuilder hb) {
    throw new IllegalStateException("Expected render to be overriden");
  }

  @Override
  public void focus() {
  }

}
