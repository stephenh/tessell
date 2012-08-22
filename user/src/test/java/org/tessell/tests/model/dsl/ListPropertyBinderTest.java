package org.tessell.tests.model.dsl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.tessell.model.properties.NewProperty.listProperty;

import java.util.ArrayList;

import org.junit.Test;
import org.tessell.gwt.user.client.ui.IsWidget;
import org.tessell.gwt.user.client.ui.StubFlowPanel;
import org.tessell.gwt.user.client.ui.StubLabel;
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
  final ListViewFactory<String> viewFactory = new ListViewFactory<String>() {
    public IsWidget create(String value) {
      StubLabel label = new StubLabel();
      label.setText(value);
      return label;
    }
  };
  final ListPresenterFactory<String> presenterFactory = new ListPresenterFactory<String>() {
    public Presenter create(String value) {
      StubLabel label = new StubLabel();
      label.setText(value);
      return new BasicPresenter<IsWidget>(label) {
      };
    }
  };

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
    ParentPresenter parent = bind(new ParentPresenter());
    binder.bind(names).to(parent, panel, presenterFactory);
    assertLabel(panel.getIsWidget(0), "one");
    assertLabel(panel.getIsWidget(1), "two");
    assertThat(parent.getChildren().size(), is(2));
  }

  @Test
  public void newPresentersAreAddedToPanel() {
    ParentPresenter parent = bind(new ParentPresenter());
    binder.bind(names).to(parent, panel, presenterFactory);
    assertThat(panel.getWidgetCount(), is(0));
    names.add("one");
    assertLabel(panel.getIsWidget(0), "one");
    assertThat(parent.getChildren().size(), is(1));
  }

  @Test
  public void oldPresentersAreRemovedFromPanel() {
    ParentPresenter parent = bind(new ParentPresenter());
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

  private static void assertLabel(IsWidget label, String text) {
    assertThat(((StubLabel) label).getText(), is(text));
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
