package org.gwtmpv.widgets;

import static org.gwtmpv.widgets.Widgets.newFocusPanel;
import static org.gwtmpv.widgets.Widgets.newPopupPanel;
import static org.gwtmpv.widgets.Widgets.newTimer;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

/** Renders a main content widget and pops up a detail panel on mouse over. */
public class AbstractPopupBox<T extends HasAllMouseHandlers & IsWidget, U extends IsWidget> extends CompositeIsWidget {

  protected final IsPopupPanel popupPanel = newPopupPanel();
  protected final IsFocusPanel popupFocus = newFocusPanel();
  protected final IsTimer hidePopupTimer = newTimer(new Runnable() {
    public void run() {
      hidePopup();
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
    hidePopupTimer.cancel();
  }

  /** On mouseover popup or textbox, show the pop--on mouseout, start the timer to hide it. */
  private final class MouseHandler implements MouseOverHandler, MouseOutHandler {
    public void onMouseOver(final MouseOverEvent event) {
      showPopup();
      hidePopupTimer.cancel();
    }

    public void onMouseOut(final MouseOutEvent event) {
      hidePopupTimer.schedule(350);
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
