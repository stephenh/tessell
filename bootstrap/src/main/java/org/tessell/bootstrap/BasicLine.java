package org.tessell.bootstrap;

import static org.tessell.bootstrap.views.AppViews.newBasicLineView;
import static org.tessell.util.WidgetUtils.hide;
import static org.tessell.util.WidgetUtils.show;

import org.tessell.bootstrap.views.IsBasicLineView;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.ui.IsHTMLPanel;
import org.tessell.gwt.user.client.ui.IsIndexedPanel;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.properties.Property;
import org.tessell.widgets.IsTextList;

import com.google.gwt.thirdparty.guava.common.annotations.VisibleForTesting;

/**
 * A generic form line that has a control group and errors, but no controls, so that they can be provided by either the
 * user directly or by subclasses.
 * 
 * Either by calling:
 * 
 * <code>view.basicLine().addControl(...)</code>
 * 
 * Or in a ui.xml file:
 * 
 * <code>
 *   <bzp:BasicLine>
 *     <!-- page-specific controls goes here -->
 *   </bzp:BasicLine>
 * </code>
 */
public class BasicLine extends AbstractLine {

  protected final IsBasicLineView view = setWidget(newBasicLineView());
  private boolean alwaysHideOptional = false;

  public BasicLine() {
    // We start out hiding optional, in case people use this in a ui.xml
    // file merely for form layout. Once they add a property, we'll reshow it.
    hide(view.optional());
    b.when(valid()).is(false).set(view.b().error()).on(view.controlGroup());
  }

  // I believe should be deprecated, left over from when we had <bzp:content>
  public void addContent(final IsWidget w) {
    addIsWidget(w);
  }

  // For rendering prepend/etc. icons
  public void setInputPrepend(final boolean prepend) {
    if (prepend) {
      view.placeholder().addStyleName(view.b().inputPrepend());
    } else {
      view.placeholder().removeStyleName(view.b().inputPrepend());
    }
  }

  public void setLabel(final String label) {
    view.label().setInnerText(label);
  }

  public IsTextList getErrorList() {
    return view.errorList();
  }

  public IsIndexedPanel getControls() {
    return view.placeholder();
  }

  public IsElement getLabel() {
    return view.label();
  }

  @VisibleForTesting
  public IsHTMLPanel controlGroup() {
    return view.controlGroup();
  }

  /**
   * Adds properties that should trigger errors for this line.
   * 
   * Note: this method is public so that clients of BasicLine itself (as well as subclasses) can pass in the properties
   * that are part of this line.
   */
  @Override
  public void addToValid(final Property<?> property) {
    super.addToValid(property);
    // Assume each property we're watching validity on, we should show errors for
    b.bind(property).errorsTo(getErrorList());
    // Kind of a hacky place to do this, but it is a convenient choke point for
    // looking at the properties that our subclasses/callers will pass to us.
    if (property.isRequired()) {
      hide(view.optional());
    } else if (!alwaysHideOptional) {
      show(view.optional());
    }
  }

  public void alwaysHideOptionalLabel() {
    hide(view.optional());
    alwaysHideOptional = true;
  }

  public void alwaysHideErrorLabel() {
    hide(view.errorList());
  }

  @Override
  protected void addIsWidget(final IsWidget w) {
    view.placeholder().add(w);
  }

  @Override
  protected void setupDisableWhenActive() {
    // BasicLine is generic, so nothing to do here...
  }

  @VisibleForTesting
  IsBasicLineView getView() {
    return view;
  }
}
