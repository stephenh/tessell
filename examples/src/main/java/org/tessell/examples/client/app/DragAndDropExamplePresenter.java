package org.tessell.examples.client.app;

import static org.tessell.examples.client.views.AppViews.newDragAndDropExampleView;
import static org.tessell.model.properties.NewProperty.basicProperty;
import static org.tessell.model.properties.NewProperty.dragging;
import static org.tessell.model.properties.NewProperty.draggingOver;

import org.tessell.examples.client.views.IsDragAndDropExampleView;
import org.tessell.gwt.user.client.ui.IsAnchor;
import org.tessell.gwt.user.client.ui.IsFlowPanel;
import org.tessell.gwt.user.client.ui.IsLabel;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.properties.Property;
import org.tessell.presenter.BasicPresenter;

import com.google.gwt.core.shared.GWT;
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
      Property<IsAnchor> current = basicProperty("current");
      set("anchors", view.rootAnchors(), current, view.a1());
      set("anchors", view.rootAnchors(), current, view.a2());
      set("anchors", view.rootAnchors(), current, view.a3());
      set("anchors", view.rootAnchors(), current, view.a4());
    }
    {
      Property<IsLabel> current = basicProperty("current");
      set("labels", view.rootLabels(), current, view.l1());
      set("labels", view.rootLabels(), current, view.l2());
      set("labels", view.rootLabels(), current, view.l3());
      set("labels", view.rootLabels(), current, view.l4());
    }
  }

  private <T extends HasAllDragAndDropHandlers & IsWidget> void set(String type, IsFlowPanel root, Property<T> current, T a) {
    // just to see one of them not be draggable
    if (a != view.a4() && a != view.l4()) {
      a.getIsElement().setAttribute("draggable", "true");
    }
    binder.when(draggingOver(a)).is(true).set(view.style().bold()).on(a);
    binder.when(dragging(a)).is(true).set(current).to(a);
    a.addDragStartHandler(e -> e.setData("text", type));
    a.addDragOverHandler(e -> e.preventDefault());
    a.addDropHandler(e -> {
      if (type.equals(e.getData("text")) && current.get() != a) {
        GWT.log("Dropped " + current + " onto " + a);
        root.remove(current.get());
        root.insert(current.get(), root.getWidgetIndex(a));
      }
      e.preventDefault();
    });
  }

}
