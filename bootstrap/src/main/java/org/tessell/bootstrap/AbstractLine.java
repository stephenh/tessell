package org.tessell.bootstrap;

import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.dsl.Binder;
import org.tessell.model.properties.Property;
import org.tessell.model.properties.PropertyGroup;
import org.tessell.widgets.CompositeHasIsWidgets;

import com.google.gwt.user.client.ui.HasEnabled;

abstract class AbstractLine extends CompositeHasIsWidgets {

  protected final Binder b = new Binder();
  private final PropertyGroup valid = new PropertyGroup("valid", "Line is invalid");
  protected Form form;

  /** @return a property for whether all of this line's properties are valid or not */
  public Property<Boolean> valid() {
    return valid;
  }

  /** Called by Form.add when GWT/Tessell constructs the widget tree. */
  public void setForm(final Form form) {
    this.form = form;
    // We wait to do setupDisableWhenInactive until we have the form
    if (form != null) {
      setupDisableWhenActive();
    }
  }

  /**
   * For subclasses to disable their controls when the form is active.
   * 
   * The form is not available in XxxLine constructors, so this acts as a 2nd stage of setup, as it's called after this
   * is added to the form.
   */
  protected abstract void setupDisableWhenActive();

  /** For subclasses to add properties to our "valid" property group. */
  protected void addToValid(final Property<?> property) {
    valid.add(property);
  }

  /** Helper method for subclasses or views that use BasicLines. */
  public void disableWhenActive(final HasEnabled hasEnabled) {
    // null check just in case we're being used outside of a form, e.g. unit testing
    if (form != null) {
      b.when(form.active()).is(true).disable(hasEnabled);
    }
  }

  @Override
  protected void addIsWidget(final IsWidget w) {
    throw new UnsupportedOperationException("Override in a line subclass if needed");
  }

}
