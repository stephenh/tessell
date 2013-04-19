package org.tessell.bootstrap;

import static org.tessell.bootstrap.views.AppViews.newTooltipView;
import static org.tessell.widgets.Widgets.getRootPanel;

import org.tessell.bootstrap.views.IsTooltipView;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.util.WidgetUtils;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;

public class Tooltip {

  public static <T extends HasMouseOutHandlers & HasMouseOverHandlers & IsWidget> Tooltip add(
      final T target,
      final String text) {
    final Tooltip tip = new Tooltip(target, text);
    // used to add a small delay after a hover event to show the tooltips
    final com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
      public void run() {
        tip.show();
      }
    };
    target.addMouseOverHandler(new MouseOverHandler() {
      public void onMouseOver(final MouseOverEvent e) {
        t.schedule(500);
      }
    });
    target.addMouseOutHandler(new MouseOutHandler() {
      public void onMouseOut(final MouseOutEvent e) {
        t.cancel();
        tip.hide();
      }
    });
    return tip;
  }

  private final IsTooltipView view = newTooltipView();
  private final IsWidget widget;

  private Tooltip(final IsWidget widget, final String text) {
    this.widget = widget;
    view.tooltipText().setText(text);
    getRootPanel().add(view);
  }

  public void show() {
    setPosition();
    view.tooltip().addStyleName("in");
  }

  public void hide() {
    view.tooltip().removeStyleName("in");
  }

  public void remove() {
    WidgetUtils.hide(view);
    view.getIsElement().removeFromParent();
  }

  public void setText(final String text) {
    view.tooltipText().setText(text);
  }

  // Warning: getOffSetWidth/Height only return values if a) The DOM is ready && b) The widget is visible (eg. not
  // display: none)
  private void setPosition() {
    // start at the left offset, then align the tooltip arrow to left edge, then shift the tooltip such that the arrow
    // is directly over the center point of the widget
    final int center = widget.getAbsoluteLeft() - view.tooltip().getOffsetWidth() / 2 + widget.getOffsetWidth() / 2;
    final int height = widget.getAbsoluteTop() - 30; // fixed
    view.tooltip().getStyle().setTop(height, Unit.PX);
    view.tooltip().getStyle().setLeft(center, Unit.PX);
  }
}
