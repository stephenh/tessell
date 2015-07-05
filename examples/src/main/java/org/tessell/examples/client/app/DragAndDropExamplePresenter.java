package org.tessell.examples.client.app;

import static org.tessell.examples.client.views.AppViews.newDragAndDropExampleView;

import org.tessell.examples.client.views.IsDragAndDropExampleView;
import org.tessell.gwt.user.client.ui.IsAnchor;
import org.tessell.gwt.user.client.ui.IsFlowPanel;
import org.tessell.gwt.user.client.ui.IsLabel;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.presenter.BasicPresenter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.HasAllDragAndDropHandlers;

/** An example of checkbox binding. */
public class DragAndDropExamplePresenter extends BasicPresenter<IsDragAndDropExampleView> {

  public DragAndDropExamplePresenter() {
    super(newDragAndDropExampleView());
  }

  @Override
  public void onBind() {
    super.onBind();
    {
      IsAnchor[] current = { null };
      set("anchors", view.rootAnchors(), current, view.a1());
      set("anchors", view.rootAnchors(), current, view.a2());
      set("anchors", view.rootAnchors(), current, view.a3());
      set("anchors", view.rootAnchors(), current, view.a4());
    }
    {
      IsLabel[] current = { null };
      set("labels", view.rootLabels(), current, view.l1());
      set("labels", view.rootLabels(), current, view.l2());
      set("labels", view.rootLabels(), current, view.l3());
      set("labels", view.rootLabels(), current, view.l4());
    }
  }

  private <T extends HasAllDragAndDropHandlers & IsWidget> void set(String type, IsFlowPanel root, T[] current, T a) {
    // just to see one of them not be draggable
    if (a != view.a4()) {
      a.getIsElement().setAttribute("draggable", "true");
    }

    a.addDragEnterHandler(e -> {
      GWT.log("data=" + e.getData("text"));
      if (a != current[0]) {
        a.getStyle().setFontWeight(FontWeight.BOLD);
      }
    });
    a.addDragLeaveHandler(e -> {
      a.getStyle().clearFontWeight();
    });
    a.addDropHandler(e -> {
      a.getStyle().clearFontWeight();
    });

    a.addDragStartHandler(e -> {
      GWT.log("start");
      e.setData("text", type);
      current[0] = a;
    });
    a.addDragEndHandler(e -> {
      GWT.log("end");
      current[0] = null;
    });

    a.addDragOverHandler(e -> {
      e.preventDefault();
    });
    a.addDropHandler(e -> {
      // invalid drop
      if (!type.equals(e.getData("text")) || current[0] == a) {
        e.preventDefault();
        return;
      }
      GWT.log("Dropped " + current + " onto " + a);
      root.remove(current[0]);
      root.insert(current[0], root.getWidgetIndex(a));
      e.preventDefault();
    });
  }

}
