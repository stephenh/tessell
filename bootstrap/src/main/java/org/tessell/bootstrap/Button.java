package org.tessell.bootstrap;

import static org.tessell.bootstrap.views.AppViews.newButtonView;

import org.tessell.bootstrap.views.IsButtonView;
import org.tessell.gwt.dom.client.IsElement;
import org.tessell.gwt.user.client.ui.IsButton;
import org.tessell.model.commands.UiCommand;
import org.tessell.model.dsl.Binder;
import org.tessell.widgets.CompositeIsWidget;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasText;

public class Button extends CompositeIsWidget implements HasClickHandlers, HasMouseOverHandlers, HasMouseOutHandlers,
    HasEnabled, HasText {

  private final IsButtonView view = newButtonView();
  private final Binder b = new Binder();
  private Form form;
  private String side;
  private Tooltip tip;
  private boolean primary;

  public Button() {
    setWidget(view);
  }

  public void bind(final UiCommand command) {
    if (form != null) {
      form.watch(command);
    }
    b.bind(command).to(view.button());
  }

  public boolean isPrimary() {
    return primary;
  }

  public void setPrimary(final boolean primary) {
    this.primary = primary;
    if (primary) {
      getButton().addStyleName(view.b().btnPrimary());
    } else {
      getButton().removeStyleName(view.b().btnPrimary());
    }
  }

  public IsButton getButton() {
    return view.button();
  }

  public IsElement getTextElement() {
    return view.text();
  }

  @Override
  public String getText() {
    return view.text().getInnerText();
  }

  @Override
  public void setText(final String text) {
    view.text().setInnerText(text);
  }

  public void setTextStyle(final String style) {
    view.text().setStyleName(style);
  }

  public void removeTextStyle(final String style) {
    view.text().removeStyleName(style);
  }

  public String getTextStyle() {
    return view.text().getStyleName();
  }

  public void setTooltip(final String text) {
    if (tip != null) {
      tip.remove();
    }
    tip = Tooltip.add(this, text);
  }

  // Populates the "caret" span with a down arrow image
  public void setDropDown(final boolean isDropDown) {
    if (isDropDown) {
      view.caret().setStyleName(view.b().caret());
    } else {
      view.caret().removeStyleName(view.b().caret());
    }
  }

  public void setIcon(final String styleName) {
    view.icon().setStyleName(styleName);
  }

  @Override
  public HandlerRegistration addClickHandler(final ClickHandler handler) {
    return view.button().addClickHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseOverHandler(final MouseOverHandler handler) {
    return view.button().addMouseOverHandler(handler);
  }

  @Override
  public HandlerRegistration addMouseOutHandler(final MouseOutHandler handler) {
    return view.button().addMouseOutHandler(handler);
  }

  @Override
  public boolean isEnabled() {
    return view.button().isEnabled();
  }

  @Override
  public void setEnabled(final boolean enabled) {
    view.button().setEnabled(enabled);
  }

  public String getSide() {
    return side;
  }

  public void setSide(final String side) {
    this.side = side;
  }

  // changing the styling state to "active"
  public void setActive() {
    view.button().addStyleName(view.b().active());
  }

  // changing the styling state to "InActive"
  public void setInActive() {
    view.button().removeStyleName(view.b().active());
  }

  public void setForm(final Form form) {
    this.form = form;
    b.when(form.active()).is(true).disable(view.button());
  }
}
