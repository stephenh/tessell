package org.tessell.model.dsl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tessell.gwt.user.client.ui.IsInsertPanel;
import org.tessell.gwt.user.client.ui.IsListBox;
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
  private final boolean[] active = { false };

  public ListPropertyBinder(final Binder b, final ListProperty<P> p) {
    super(b, p);
    this.p = p;
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public void toMultiple(final IsListBox source, final List<P> options) {
    toMultiple(source, options, new ListBoxIdentityAdaptor<P>());
  }

  public void toMultiple(final IsListBox source, final List<P> options, final ListBoxLambdaAdaptor<P> adaptor) {
    toMultiple(source, options, (ListBoxAdaptor<P, P>) adaptor);
  }

  /** Binds our {@code p} to the selection in {@code source}, given the {@code options}. */
  public <O> void toMultiple(final IsListBox source, final List<O> options, final ListBoxAdaptor<P, O> adaptor) {
    source.setMultipleSelect(true);
    addOptionsAndSetIfNull(source, options, adaptor);
    b.add(source.addChangeHandler(e -> {
      if (!active[0]) {
        active[0] = true;
        // collect all currently-selected options
        List<P> newOptions = new ArrayList<P>();
        for (int i = 0; i < source.getItemCount(); i++) {
          if (source.isItemSelected(i)) {
            newOptions.add(adaptor.toValue(options.get(i)));
          }
        }
        p.set(newOptions);
        active[0] = false;
      }
    }));
    b.add(p.addPropertyChangedHandler(e -> {
      if (!active[0]) {
        active[0] = true;
        setToFirstIfNull(options, adaptor);
        for (int i = 0; i < options.size(); i++) {
          boolean contains = p.get().contains(adaptor.toValue(options.get(i)));
          source.setItemSelected(i, contains);
        }
        active[0] = false;
      }
    }));
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
    b.add(p.addListChangedHandler(e -> e.getDiff().apply(panel, a -> factory.create(a))));
  }

  public void to(final ListLike<P> panel) {
    if (p.get() != null) {
      int i = 0;
      for (P value : p.get()) {
        panel.add(i++, value);
      }
    }
    b.add(p.addListChangedHandler(e -> e.getDiff().apply(panel, a -> a)));
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

  private <O> void addOptionsAndSetIfNull(final IsListBox source, final List<O> options, final ListBoxAdaptor<P, O> adaptor) {
    int i = 0;
    for (final O option : options) {
      source.addItem(adaptor.toDisplay(option), Integer.toString(i++));
    }
    setToFirstIfNull(options, adaptor);
    for (int j = 0; j < options.size(); j++) {
      boolean contains = p.get().contains(adaptor.toValue(options.get(j)));
      source.setItemSelected(j, contains);
    }
  }

  /** If our property is null, but the list of options doesn't contain {@code null}, auto-select the first valid value. */
  private <O> void setToFirstIfNull(List<O> options, final ListBoxAdaptor<P, O> adaptor) {
    // Just to be cautious, call setInitialValue with an active check to prevent stack overflows
    // if the application code has a handler that tries to keep setting it back to null
    if (!active[0]) {
      active[0] = true;
      if (p.get() == null && !options.contains(null) && !options.isEmpty()) {
        List<P> initial = new ArrayList<P>();
        initial.add(adaptor.toValue(options.get(0)));
        p.setInitialValue(initial);
      }
      active[0] = false;
    }
  }

}
