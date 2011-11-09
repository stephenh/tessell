package org.gwtmpv.widgets.form.lines;

import static org.gwtmpv.util.Inflector.camelize;
import static org.gwtmpv.util.StringUtils.uncapitalize;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.Property;
import org.gwtmpv.model.properties.PropertyGroup;
import org.gwtmpv.util.HTMLPanelBuilder;
import org.gwtmpv.widgets.form.FormLayout;
import org.gwtmpv.widgets.form.FormPresenter;

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
  public void bind(final FormPresenter p, PropertyGroup all, Binder binder) {
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
