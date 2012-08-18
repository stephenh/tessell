package org.tessell.widgets.form.lines;

import static org.tessell.util.Inflector.camelize;
import static org.tessell.util.StringUtils.uncapitalize;

import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.util.HTMLPanelBuilder;
import org.tessell.widgets.form.FormLayout;
import org.tessell.widgets.form.FormPresenter;

/**
 * A base form line implementation that assumes it's editing a {@link Property}.
 */
public abstract class AbstractFormLine<T> implements FormLine {

  protected final Property<T> property;
  protected String idSuffix;
  protected String label;
  protected String id;

  protected AbstractFormLine(Property<T> property) {
    this.property = property;
    setLabel(property.getName());
  }

  @Override
  public void bind(final FormPresenter p, PropertyGroup all) {
    id = p.getId() + "-" + idSuffix;
    all.add(property);
  }

  @Override
  public void render(FormPresenter p, FormLayout layout, HTMLPanelBuilder hb) {
    layout.lineDefault(p, this, hb);
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    setLabel(label, true);
  }

  public void setLabel(String label, boolean useAsId) {
    this.label = label;
    if (useAsId) {
      idSuffix = uncapitalize(camelize(label));
    }
  }

}
