package org.tessell.bootstrap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.tessell.gwt.dom.client.GwtElement;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.dom.client.IsStyle;
import org.tessell.widgets.IsTextList;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/** A custom {@link IsTextList} that renders items as simple text {@code foo, bar, zaz}. */
public class TextCommaList extends Widget implements IsTextList {

  private boolean enabled = true;
  private final List<String> errors = new ArrayList<String>();

  public TextCommaList() {
    setElement(DOM.createDiv());
    updateShowing();
  }

  @Override
  public void add(final String text) {
    errors.add(text);
    updateShowing();
  }

  @Override
  public void remove(final String text) {
    errors.remove(text);
    updateShowing();
  }

  @Override
  public void clear() {
    errors.clear();
    updateShowing();
  }

  @Override
  public String getChildTag() {
    return null;
  }

  @Override
  public void setChildTag(final String childTag) {
  }

  @Override
  public String getChildStyleName() {
    return null;
  }

  @Override
  public void setChildStyleName(final String childStyleName) {
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  @Override
  public IsStyle getStyle() {
    return getIsElement().getStyle();
  }

  @Override
  public IsElement getIsElement() {
    return new GwtElement(getElement());
  }

  @Override
  public boolean hasErrors() {
    return errors.size() > 0;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  private void updateShowing() {
    if (errors.size() == 0) {
      getElement().setInnerHTML("&nbsp;");
    } else {
      String text = "";
      for (Iterator<String> i = errors.iterator(); i.hasNext();) {
        text += SafeHtmlUtils.htmlEscape(i.next());
        if (i.hasNext()) {
          text += ", ";
        }
      }
      getElement().setInnerHTML(text);
    }
  }

}
