package org.tessell.tests.model.dsl;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.tessell.model.properties.NewProperty.listProperty;

import java.util.ArrayList;
import java.util.List;

import joist.util.Copy;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.gwt.user.client.ui.StubFlowPanel;
import org.tessell.gwt.user.client.ui.StubLabel;
import org.tessell.gwt.user.client.ui.StubListBox;
import org.tessell.model.dsl.Binder;
import org.tessell.model.dsl.ListPropertyBinder.ListPresenterFactory;
import org.tessell.model.dsl.ListPropertyBinder.ListViewFactory;
import org.tessell.model.properties.ListProperty;
import org.tessell.presenter.BasicPresenter;
import org.tessell.presenter.Presenter;

public class ListPropertyBinderTest {

  final Binder binder = new Binder();
  final StubFlowPanel panel = new StubFlowPanel();
  final ListProperty<String> names = listProperty("names");
  final StubViewFactory viewFactory = new StubViewFactory();
  final StubPresenterFactory presenterFactory = new StubPresenterFactory();
  final ParentPresenter parent = bind(new ParentPresenter());
  final StubListBox box = new StubListBox();

  @Test
  public void initialNamesAreAddedToPanel() {
    names.add("one");
    names.add("two");
    binder.bind(names).to(panel, viewFactory);
    assertLabel(panel.getIsWidget(0), "one");
    assertLabel(panel.getIsWidget(1), "two");
  }

  @Test
  public void newNamesAreAddedToPanel() {
    binder.bind(names).to(panel, viewFactory);
    assertThat(panel.getWidgetCount(), is(0));
    names.add("one");
    assertLabel(panel.getIsWidget(0), "one");
  }

  @Test
  public void newNamesAreAddedWithIndexToPanel() {
    binder.bind(names).to(panel, viewFactory);
    names.add("one");
    assertLabel(panel.getIsWidget(0), "one");
    names.add(0, "two");
    assertLabel(panel.getIsWidget(0), "two");
  }

  @Test
  public void newNamesAreAddedAfterExistingContent() {
    panel.add(viewFactory.create("existing"));
    binder.bind(names).to(panel, viewFactory);
    names.add("one");
    assertLabel(panel.getIsWidget(0), "existing");
    assertLabel(panel.getIsWidget(1), "one");
  }

  @Test
  public void reordersToDoNotRequireCreatingNewViews() {
    binder.bind(names).to(panel, viewFactory);
    names.set(list("one", "two"));
    assertThat(panel.getWidgetCount(), is(2));
    assertLabel(panel.getIsWidget(0), "one");
    assertThat(viewFactory.created, is(2));

    names.set(list("two", "one"));
    assertThat(panel.getWidgetCount(), is(2));
    assertLabel(panel.getIsWidget(0), "two");
    assertThat(viewFactory.created, is(2));
  }

  @Test
  public void reordersToDoNotRequireCreatingNewPresenters() {
    binder.bind(names).to(parent, panel, presenterFactory);
    names.set(list("one", "two"));
    assertThat(panel.getWidgetCount(), is(2));
    assertLabel(panel.getIsWidget(0), "one");
    assertThat(presenterFactory.created, is(2));

    names.set(list("two", "one"));
    assertThat(panel.getWidgetCount(), is(2));
    assertLabel(panel.getIsWidget(0), "two");
    assertThat(presenterFactory.created, is(2));
  }

  @Test
  public void oldNamesAreRemovedFromPanel() {
    binder.bind(names).to(panel, viewFactory);
    names.add("one");
    names.add("two");
    assertThat(panel.getWidgetCount(), is(2));
    names.remove("one");
    assertThat(panel.getWidgetCount(), is(1));
    assertLabel(panel.getIsWidget(0), "two");
  }

  @Test
  public void initialPresentersAreAddedToPanel() {
    names.add("one");
    names.add("two");
    binder.bind(names).to(parent, panel, presenterFactory);
    assertLabel(panel.getIsWidget(0), "one");
    assertLabel(panel.getIsWidget(1), "two");
    assertThat(parent.getChildren().size(), is(2));
  }

  @Test
  public void newPresentersAreAddedToPanel() {
    binder.bind(names).to(parent, panel, presenterFactory);
    assertThat(panel.getWidgetCount(), is(0));
    names.add("one");
    assertLabel(panel.getIsWidget(0), "one");
    assertThat(parent.getChildren().size(), is(1));
  }

  @Test
  public void oldPresentersAreRemovedFromPanel() {
    binder.bind(names).to(parent, panel, presenterFactory);
    names.add("one");
    names.add("two");
    assertThat(panel.getWidgetCount(), is(2));
    assertThat(parent.getChildren().size(), is(2));
    names.remove("one");
    assertThat(panel.getWidgetCount(), is(1));
    assertLabel(panel.getIsWidget(0), "two");
    assertThat(parent.getChildren().size(), is(1));
  }

  @Test
  public void bindDoesNotNullPointerExceptionForViews() {
    names.set(null);
    binder.bind(names).to(panel, viewFactory);
    names.set(Copy.list("one"));
    assertThat(panel.getWidgetCount(), is(1));
  }

  @Test
  public void bindDoesNotNullPointerExceptionForPresenters() {
    names.set(null);
    ParentPresenter parent = bind(new ParentPresenter());
    binder.bind(names).to(parent, panel, presenterFactory);
    names.set(Copy.list("one"));
    assertThat(panel.getWidgetCount(), is(1));
  }

  @Test
  public void toListBox() {
    List<String> options = Copy.list("one", "two", "three");
    names.add("one");
    binder.bind(names).toMultiple(box, options);
    assertThat(box.getSelectedTexts(), contains("one"));

    names.add("two");
    assertThat(box.getSelectedTexts(), contains("one", "two"));

    box.setItemSelected(2, true);
    assertThat(names.get(), contains("one", "two", "three"));
  }

  @Test
  public void toListBoxWithAdaptor() {
    List<String> options = Copy.list("one", "two", "three");
    names.add("one");
    binder.bind(names).toMultiple(box, options, o -> o.toUpperCase());
    assertThat(box.getSelectedTexts(), contains("ONE"));

    names.add("two");
    assertThat(box.getSelectedTexts(), contains("ONE", "TWO"));

    box.setItemSelected(2, true);
    assertThat(names.get(), contains("one", "two", "three"));
  }

  private static void assertLabel(IsWidget label, String text) {
    assertThat(((StubLabel) label).getText(), is(text));
  }

  private final class StubPresenterFactory implements ListPresenterFactory<String> {
    int created = 0;

    public Presenter create(String value) {
      created++;
      StubLabel label = new StubLabel();
      label.setText(value);
      return new BasicPresenter<IsWidget>(label) {
      };
    }
  }

  private final class StubViewFactory implements ListViewFactory<String> {
    int created = 0;

    public IsWidget create(String value) {
      created++;
      StubLabel label = new StubLabel();
      label.setText(value);
      return label;
    }
  }

  private static final class ParentPresenter extends BasicPresenter<IsWidget> {
    public ParentPresenter() {
      super(new StubFlowPanel());
    }

    private ArrayList<Presenter> getChildren() {
      return children();
    }
  }

  private static <P extends Presenter> P bind(P p) {
    p.bind();
    return p;
  }
}
