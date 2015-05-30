package org.tessell.model.dsl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tessell.gwt.user.client.ui.IsInsertPanel;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.model.properties.ListProperty;
import org.tessell.presenter.BasicPresenter;
import org.tessell.presenter.Presenter;
import org.tessell.util.ListDiff;
import org.tessell.util.ListDiff.ListLike;
import org.tessell.util.ListDiff.Location;

/** Fluent binding methods for {@link ListProperty}s. */
public class ListPropertyBinder<P> extends PropertyBinder<List<P>> {

  /** Factory for creating {@link IsWidget}s for each item in a list. */
  public interface ListViewFactory<P> {
    IsWidget create(P value);
  }

  /** Factory for creating {@link Presenter}s for each item in a list. */
  public interface ListPresenterFactory<P> {
    Presenter create(P value);
  }

  private final ListProperty<P> p;

  public ListPropertyBinder(final Binder b, final ListProperty<P> p) {
    super(b, p);
    this.p = p;
  }

  /** Binds each value in {@code p} to a view created by {@code factory}. */
  public void to(final IsInsertPanel panel, final ListViewFactory<P> factory) {
    to(new InsertPanelListLikeAdapter(panel), factory);
  }

  /** Binds each value in {@code p} to a view created by {@code factory}. */
  public void to(final ListLike<IsWidget> panel, final ListViewFactory<P> factory) {
    if (p.get() != null) {
      int i = 0;
      for (P value : p.get()) {
        panel.add(i++, factory.create(value));
      }
    }
    b.add(p.addListChangedHandler(e -> {
      e.getDiff().apply(panel, a -> factory.create(a));
    }));
  }

  /**
   * Binds each value in {@code p} to a presenter created by {@code factory}.
   *
   * Also adds/removes the child presenters to the {@code parent} presenter for proper binding/unbinding.
   */
  public void to(final BasicPresenter<?> parent, final IsInsertPanel panel, final ListPresenterFactory<P> factory) {
    final InsertPanelListLikeAdapter adapter = new InsertPanelListLikeAdapter(panel);
    // map to remember the model->presenter mapping so we know which view to remove later
    final Map<P, Presenter> views = new HashMap<P, Presenter>();
    if (p.get() != null) {
      for (P value : p.get()) {
        Presenter child = factory.create(value);
        parent.addPresenter(child);
        views.put(value, child);
        panel.add(child.getView());
      }
    }
    b.add(p.addListChangedHandler(e -> {
      e.getDiff().apply(adapter, value -> {
        Presenter child = factory.create(value);
        parent.addPresenter(child);
        views.put(value, child);
        return child.getView();
      });
      for (Location<P> remove : e.getDiff().removed) {
        Presenter child = views.remove(remove.element);
        if (child != null) {
          parent.removePresenter(child);
        }
      }
    }));
  }

  private static class InsertPanelListLikeAdapter implements ListDiff.ListLike<IsWidget> {
    private final IsInsertPanel panel;
    private final int offsetForExistingContent;

    private InsertPanelListLikeAdapter(IsInsertPanel panel) {
      this.panel = panel;
      this.offsetForExistingContent = panel.getWidgetCount();
    }

    @Override
    public IsWidget remove(int index) {
      IsWidget w = panel.getIsWidget(index + offsetForExistingContent);
      panel.remove(index + offsetForExistingContent);
      return w;
    }

    @Override
    public void add(int index, IsWidget a) {
      panel.insert(a, index + offsetForExistingContent);
    }
  }

}
