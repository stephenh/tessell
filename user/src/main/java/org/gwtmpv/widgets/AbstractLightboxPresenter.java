package org.gwtmpv.widgets;

import static org.gwtmpv.widgets.Widgets.getWindow;
import static org.gwtmpv.widgets.Widgets.newFadingDialogBox;
import static org.gwtmpv.widgets.Widgets.newScrollPanel;

import org.gwtmpv.presenter.BasicPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.PopupPanel;

/** For showing views in a lightbox popup. */
public abstract class AbstractLightboxPresenter<V extends IsWidget> extends BasicPresenter<V> {

  protected final IsWindow window = getWindow();
  protected final IsFadingDialogBox box = newFadingDialogBox();
  protected final IsScrollPanel scroll = newScrollPanel();

  public AbstractLightboxPresenter(final V view) {
    super(view);
  }

  protected void setWidth(final int pixels) {
    scroll.getStyle().setWidth(pixels, Unit.PX);
  }

  @Override
  public void onBind() {
    super.onBind();

    box.setAnimationEnabled(true);
    box.setAutoFadeInElement(false); // we'll fade in the element when ready
    box.setGlassEnabled(true);
    box.setWidget(scroll);
    box.getStyle().setZIndex(2);

    scroll.setAlwaysShowScrollBars(false);
    // If the vertical scroll bar shows up, it will take some horizontal space
    // and cause our content to overflow by 5-whatever pixels. Ignore that.
    scroll.getStyle().setProperty("overflowX", Overflow.HIDDEN.getCssName());
    scroll.getStyle().setBackgroundColor("#FFFFFF");
    scroll.add(view);

    if (GWT.isClient()) {
      // fix weird bug where the margin:0 in the css reset made the vertical scroll bar always show up
      ((Element) scroll.getIsElement().asElement().getChild(0)).getStyle().setMargin(1, Unit.PX);
    }

    // selenium tests attempting to type into light boxes will hit the
    // dummy-click-div and cause the lightbox to close unless we add it
    // as an auto-hide partner. Hide this hack behind a GWT.isClient.
    if (GWT.isClient()) {
      final Element clickdiv = DOM.getElementById("dummy-click-div");
      if (clickdiv != null) {
        ((PopupPanel) box).addAutoHidePartner(clickdiv);
      }
    }

    registerHandler(box.addCloseHandler(new OnBoxClose()));
    registerHandler(window.addResizeHandler(new OnResize()));
  }

  protected void center() {
    box.center();

    final int offsetHeight = view.getOffsetHeight();
    // 100 = how far we position ourselves from the top (see next step), 20 = 10 top gray bar + 10 bottom gray bar
    final int availHeight = window.getClientHeight() - 100 - 20;
    if (availHeight < offsetHeight) {
      scroll.getStyle().setHeight(Math.max(availHeight, 0), Unit.PX);
    } else {
      scroll.getStyle().clearHeight();
    }
    scroll.onResize();

    // We used to just center, but instead want horizontal center and top to be from top
    // copy/paste from box.center() with top hardcoded to 100
    final int left = (window.getClientWidth() - view.getOffsetWidth()) >> 1;
    final int top = 100; // (window.getClientHeight() - box.getOffsetHeight()) >> 1;
    box.setPopupPosition(Math.max(window.getScrollLeft() + left, 0), Math.max(window.getScrollTop() + top, 0));
  }

  protected void centerAndFadeIfFirst() {
    if (!box.isShowing()) {
      center();
      fadeInElement();
    } else {
      center();
    }
  }

  protected void hide() {
    box.hide();
  }

  protected void fadeInElement() {
    box.fadeInElement();
  }

  protected void fadeOutElement() {
    box.fadeOutElement();
  }

  /** Means the user cannot close the popup by clicking around. */
  protected void setSuperModal() {
    box.setAutoHideEnabled(false);
  }

  /** Make sure we stay centered. */
  private class OnResize implements ResizeHandler {
    public void onResize(final ResizeEvent event) {
      if (box.isShowing()) {
        center();
      }
    }
  }

  /** Unbinds ourself when the box closes. */
  private class OnBoxClose implements CloseHandler<PopupPanel> {
    public void onClose(final CloseEvent<PopupPanel> event) {
      unbind();
    }
  }

  // for testing
  public IsFadingDialogBox getBox() {
    return box;
  }

}
