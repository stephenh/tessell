package org.gwtmpv.tests.model.dsl;

import static org.gwtmpv.model.properties.NewProperty.listProperty;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.dsl.ListPropertyBinder.ListViewFactory;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.widgets.IsWidget;
import org.gwtmpv.widgets.StubFlowPanel;
import org.gwtmpv.widgets.StubLabel;
import org.junit.Test;

public class ListPropertyBinderTest {

  final Binder binder = new Binder(new StubCanRegisterHandlers());
  final StubFlowPanel panel = new StubFlowPanel();
  final ListProperty<String> names = listProperty("names");
  final ListViewFactory<String> viewFactory = new ListViewFactory<String>() {
    public IsWidget create(String value) {
      StubLabel label = new StubLabel();
      label.setText(value);
      return label;
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

  private static void assertLabel(IsWidget label, String text) {
    assertThat(((StubLabel) label).getText(), is(text));
  }
}
