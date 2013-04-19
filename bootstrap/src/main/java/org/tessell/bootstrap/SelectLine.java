package org.tessell.bootstrap;

import static org.tessell.widgets.Widgets.newListBox;

import java.util.Arrays;
import java.util.List;

import org.tessell.gwt.user.client.ui.IsListBox;
import org.tessell.model.dsl.ListBoxAdaptor;
import org.tessell.model.dsl.ListBoxHumanizerAdaptor;
import org.tessell.model.dsl.ListBoxIdentityAdaptor;
import org.tessell.model.properties.EnumProperty;
import org.tessell.model.properties.Property;

import com.google.gwt.user.client.ui.HasEnabled;

/** Provides boilerplate HTML/behavior for select boxes in bizstrap forms. */
public class SelectLine extends BasicLine implements HasEnabled {

  protected final IsListBox box = newListBox();

  public SelectLine() {
    addContent(box);
  }

  /** To bind the select box to an enum. */
  public <P extends Enum<P>> void bindEnum(final EnumProperty<P> property, final P... values) {
    bind(property, Arrays.asList(values), new ListBoxHumanizerAdaptor<P>());
  }

  /** To bind the select box to a list of values. */
  public <P> void bind(final Property<P> property, final List<P> options) {
    bind(property, options, new ListBoxIdentityAdaptor<P>());
  }

  /** To bind the select box to a list of adapted values. */
  public <P, O> void bind(final Property<P> property, final List<O> options, final ListBoxAdaptor<P, O> adaptor) {
    addToValid(property);
    // don't overwrite the label if it's already been set
    if (view.label().getText().length() == 0) {
      view.label().setText(property.getName());
    }
    b.bind(property).to(box, options, adaptor);
  }

  /** Sets the text box style, for use in {@code ui.xml} files. */
  public void setListBoxStyleName(final String styleName) {
    box.setStyleName(styleName);
  }

  @Override
  public boolean isEnabled() {
    return box.isEnabled();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    box.setEnabled(enabled);
  }

  @Override
  protected void onEnsureDebugId(final String id) {
    super.onEnsureDebugId(id);
    box.ensureDebugId(id + "-listBox");
  }

  @Override
  protected void setupDisableWhenActive() {
    disableWhenActive(box);
  }
}
