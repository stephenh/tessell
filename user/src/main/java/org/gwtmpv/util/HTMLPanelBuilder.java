package org.gwtmpv.util;

import static org.gwtmpv.widgets.Widgets.newHTMLPanel;

import java.util.ArrayList;

import org.gwtmpv.widgets.IsHTMLPanel;
import org.gwtmpv.widgets.IsWidget;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Builds an {@link HTMLPanel} iteratively, much like UiBinder, but at runtime.
 */
public class HTMLPanelBuilder {

  /** Reset the dummy div id, for reproducible tests. */
  public static void resetId() {
    nextId = 0;
  }

  private static int nextId;
  private final StringBuilder sb = new StringBuilder();
  private final ArrayList<ToPlace> places = new ArrayList<ToPlace>();

  public void add(String html) {
    sb.append(html);
  }

  public void add(IsWidget widget) {
    final String id = "mpv-hb-" + String.valueOf(++nextId);
    sb.append("<div id=\"" + id + "\"></div>");
    places.add(new ToPlace(id, widget));
  }

  /** @return the buffered html and widgets as one {@link IsHTMLPanel}. */
  public IsHTMLPanel toHTMLPanel() {
    IsHTMLPanel p = newHTMLPanel(sb.toString());
    for (ToPlace t : places) {
      p.addAndReplaceElement(t.widget, t.id);
    }
    return p;
  }

  /** A DTO to hold pending widget adds + their temporary dom id. */
  private static class ToPlace {
    private final String id;
    private final IsWidget widget;

    private ToPlace(String id, IsWidget widget) {
      this.id = id;
      this.widget = widget;
    }
  }

}
