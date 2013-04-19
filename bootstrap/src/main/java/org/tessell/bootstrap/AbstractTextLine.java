package org.tessell.bootstrap;

import static org.tessell.bootstrap.views.AppViews.newInlineHelpFragmentView;
import static org.tessell.util.WidgetUtils.focus;

import org.tessell.bootstrap.views.IsInlineHelpFragmentView;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.ui.IsTextBoxBase;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.properties.Property;
import org.tessell.widgets.IsTextList;

import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;

/** Provides boilerplate HTML/behavior for text boxes in bootstrap forms. */
abstract class AbstractTextLine extends BasicLine implements HasEnabled, HasText, CanTriggerDefaultAction, CanAutoFocus {

  private IsTextBoxBase textBox;
  private final IsInlineHelpFragmentView help = newInlineHelpFragmentView();

  protected void onEnsureDebugId(final String id) {
    super.onEnsureDebugId(id);
    textBox.ensureDebugId(id + "-textBox");
  }

  // binds property to the text box
  public void bind(final Property<String> property) {
    addToValid(property);
    // don't overwrite the label if it's already been set
    if (view.label().getText().length() == 0) {
      view.label().setText(property.getName());
    }
    // TODO eventually update errors on key press instead of change
    b.bind(property).to(textBox);
  }

  @Override
  public void addDefaultActionHandler(final KeyDownHandler handler) {
    textBox.addKeyDownHandler(handler);
  }

  @Override
  public void autoFocus() {
    focus(textBox);
  }

  @Override
  public String getText() {
    return textBox.getText();
  }

  @Override
  public void setText(final String text) {
    textBox.setText(text);
  }

  /** Sets the label, for use in {@code ui.xml} files. */
  public void setLabel(final String label) {
    view.label().setText(label);
  }

  /** Sets the text box style, for use in {@code ui.xml} files. */
  public void setTextBoxStyleName(final String styleName) {
    textBox.setStyleName(styleName);
  }

  /** Sets the placeholder. */
  public void setPlaceholder(final String placeholder) {
    textBox.getIsElement().setAttribute("placeholder", placeholder);
  }

  @Override
  public boolean isEnabled() {
    return textBox.isEnabled();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    textBox.setEnabled(enabled);
  }

  public IsTextBoxBase getTextBox() {
    return textBox;
  }

  public IsTextList getErrorList() {
    return view.errorList();
  }

  public IsElement getHelp() {
    return help.help();
  }

  public void setHelp(final String help) {
    // Assume help is text, e.g. from an ui.xml file
    getHelp().setText(help);
  }

  // For subclasses to pass in their specific textBox implementation, contained in parent
  protected void setTextBox(final IsTextBoxBase textBox, final IsWidget parent) {
    this.textBox = textBox;
    addContent(parent);
    view.placeholder().add(help);
  }

  @Override
  protected void setupDisableWhenActive() {
    disableWhenActive(textBox);
  }
}
