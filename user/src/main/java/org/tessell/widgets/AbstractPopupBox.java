package org.tessell.widgets;

import static org.tessell.widgets.Widgets.newFocusPanel;
import static org.tessell.widgets.Widgets.newPopupPanel;
import static org.tessell.widgets.Widgets.newTimer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/** Renders a main content widget and pops up a detail panel on mouse over. */
public class AbstractPopupBox<T extends HasClickHandlers & HasAllMouseHandlers & IsWidget, U extends IsWidget> extends CompositeIsWidget {

  private static final int HIDE_DELAY_MILLIS = 500;
  protected final IsPopupPanel popupPanel = newPopupPanel();
  protected final IsFocusPanel popupFocus = newFocusPanel();
  protected final IsTimer hidePopupTimer = newTimer(new Runnable() {
    public void run() {
      hidePopupIfNeeded();
    }
  });
  protected final T mainContent;
  protected final U popupContent;

  protected AbstractPopupBox(final T mainContent, final U popupContent) {
    this.mainContent = mainContent;
    this.popupContent = popupContent;

    setWidget(mainContent);

    popupPanel.add(popupFocus);
    popupFocus.add(popupContent);

    final MouseHandler m = new MouseHandler();
    popupFocus.addMouseOverHandler(m);
    popupFocus.addMouseOutHandler(m);
    mainContent.addMouseOverHandler(m);
    mainContent.addMouseOutHandler(m);
    mainContent.addClickHandler(m);
  }

  protected final void showPopupIfNeeded() {
    if (!popupPanel.isShowing()) {
      showPopup();
    }
    hidePopupTimer.cancel();
  }

  protected final void hidePopupIfNeeded() {
    if (popupPanel.isShowing()) {
      hidePopup();
    }
    hidePopupTimer.cancel();
  }

  /** Shows the popup panel, setting it's top/left relative to the textbox's position. */
  protected void showPopup() {
    final int left = mainContent.getAbsoluteLeft();
    final int top = mainContent.getAbsoluteTop() + mainContent.getOffsetHeight() + 5;
    popupPanel.setPopupPosition(left, top);
    popupPanel.show();
  }

  /** Hides the popup panel. */
  protected void hidePopup() {
    popupPanel.hide();
  }

  /** On click/mouseover show the popup; on mouseout, start the timer to hide it. */
  private final class MouseHandler implements MouseOverHandler, MouseOutHandler, ClickHandler {
    @Override
    public void onMouseOver(final MouseOverEvent event) {
      showPopupIfNeeded();
    }

    @Override
    public void onClick(ClickEvent event) {
      showPopupIfNeeded();
    }

    @Override
    public void onMouseOut(final MouseOutEvent event) {
      hidePopupTimer.schedule(HIDE_DELAY_MILLIS);
    }
  }

  public T getMainContent() {
    return mainContent;
  }

  public U getPopupContent() {
    return popupContent;
  }

  public IsPopupPanel getPopupPanel() {
    return popupPanel;
  }

}
