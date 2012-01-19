package org.tessell.widgets.form.lines;

import java.util.List;

import org.tessell.model.dsl.Binder;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.FormLayout;
import org.tessell.widgets.form.FormPresenter;

/**
 * For laying out multiple properties within a single form line.
 * 
 * Currently hardcoded to layout things horizontally.
 */
public class CompositeFormLine implements FormLine {

  private final String label;
  private final List<FormLine> lines;
  private FormPresenter formPresenter;
  private FormLayout layout;

  public CompositeFormLine(final String label, final List<FormLine> lines) {
    this.label = label;
    this.lines = lines;
  }

  @Override
  public void bind(final FormPresenter p, final PropertyGroup all, final Binder binder) {
    for (final FormLine line : lines) {
      line.bind(p, all, binder);
    }
  }

  @Override
  public void render(final FormPresenter p, final FormLayout l, final HTMLPanelBuilder hb) {
    formPresenter = p;
    layout = l;
    l.lineDefault(p, this, hb);
  }

  @Override
  public void renderLabel(final HTMLPanelBuilder hb) {
    hb.add(label);
  }

  @Override
  public void renderValue(final HTMLPanelBuilder hb) {
    hb.add("<table border='0' cellpadding='0' cellspacing='0'><tr>");
    for (final FormLine line : lines) {
      hb.add("<td valign='top'>");
      line.renderValue(hb);
      layout.errorsBegin(formPresenter, hb);
      line.renderErrors(hb);
      layout.errorsEnd(formPresenter, hb);
      hb.add("</td>");
    }
    hb.add("</tr></table>");
  }

  @Override
  public void renderErrors(final HTMLPanelBuilder hb) {
  }

  @Override
  public void focus() {
    if (lines.size() > 0) {
      lines.get(0).focus();
    }
  }
}