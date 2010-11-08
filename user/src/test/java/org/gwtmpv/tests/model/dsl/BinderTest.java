package org.gwtmpv.tests.model.dsl;

import static org.gwtmpv.model.properties.NewProperty.booleanProperty;
import static org.gwtmpv.model.properties.NewProperty.stringProperty;
import static org.gwtmpv.testing.MpvMatchers.hasStyle;
import static org.gwtmpv.testing.MpvMatchers.hidden;
import static org.gwtmpv.testing.MpvMatchers.shown;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.gwtmpv.model.dsl.Binder;
import org.gwtmpv.model.properties.BooleanProperty;
import org.gwtmpv.model.properties.StringProperty;
import org.gwtmpv.widgets.StubTextBox;
import org.gwtmpv.widgets.StubWidget;
import org.junit.Test;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class BinderTest {

  final Binder binder = new Binder(new StubCanRegisterHandlers());
  final StringProperty s = stringProperty("s");
  final StubTextBox box = new StubTextBox();

  @Test
  public void propertyToWidget() {
    binder.bind(s).to(box);
    s.set("test");
    assertThat(box.getValue(), is("test"));
  }

  @Test
  public void propertyToWidgetIsInitiallyUntouched() {
    binder.bind(s).to(box);
    assertThat(s.isTouched(), is(false));
  }

  @Test
  public void propertyToWidgetImmediatelySetsTheWidgetsValue() {
    s.set("test");
    binder.bind(s).to(box);
    assertThat(box.getValue(), is("test"));
  }

  @Test
  public void propertyToWidgetFiresWidgetChange() {
    binder.bind(s).to(box);
    final boolean[] changed = { false };
    box.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        changed[0] = true;
      }
    });
    s.set("test");
    assertThat(changed[0], is(true));
  }

  @Test
  public void widgetToProperty() {
    binder.bind(s).to(box);
    box.type("test");
    assertThat(s.get(), is("test"));
  }

  @Test
  public void stringPropertyToWidgetSetsMaxLength() {
    s.max(100);
    binder.bind(s).to(box);
    assertThat(box.getMaxLength(), is(100));
  }

  @Test
  public void clickableWidgetToProperty() {
    binder.bind(s).withValue("gotclicked").to(box);
    box.click();
    assertThat(s.get(), is("gotclicked"));
  }

  @Test
  public void whenTrueFiresInitialValueWhenTrue() {
    BooleanProperty b = booleanProperty("b", true);
    StubWidget w = new StubWidget();
    binder.when(b).is(true).set("c").on(w);
    assertThat(w, hasStyle("c"));
  }

  @Test
  public void whenTrueDoesNotFireInitialValueWhenFalse() {
    BooleanProperty b = booleanProperty("b", false);
    StubWidget w = new StubWidget();
    binder.when(b).is(true).set("c").on(w);
    assertThat(w, not(hasStyle("c")));
  }

  @Test
  public void whenTrueFiresWhenFalseChangesToTrue() {
    BooleanProperty b = booleanProperty("b", false);
    StubWidget w = new StubWidget();
    binder.when(b).is(true).set("c").on(w);
    b.set(true);
    assertThat(w, hasStyle("c"));
  }

  @Test
  public void whenTrueShowHidesWhenFalse() {
    BooleanProperty b = booleanProperty("b", false);
    StubWidget w = new StubWidget();
    binder.when(b).is(true).show(w);
    assertThat(w, is(hidden()));
  }

  @Test
  public void whenTrueShowDisplaysWhenTrue() {
    BooleanProperty b = booleanProperty("b", true);
    StubWidget w = new StubWidget();
    binder.when(b).is(true).show(w);
    assertThat(w, is(shown()));
  }

  @Test
  public void whenTrueShowHidesWhenChangesToFalse() {
    BooleanProperty b = booleanProperty("b", true);
    StubWidget w = new StubWidget();
    binder.when(b).is(true).show(w);
    b.set(false);
    assertThat(w, is(hidden()));
  }

  @Test
  public void whenTrueAddDoesInitialSet() {
    BooleanProperty b = booleanProperty("b", true);
    ArrayList<String> list = new ArrayList<String>();
    binder.when(b).is(true).add("foo").to(list);
    assertThat(list, hasItem("foo"));
  }

  @Test
  public void whenTrueRemovesFromInitialSet() {
    BooleanProperty b = booleanProperty("b", false);
    ArrayList<String> list = new ArrayList<String>();
    list.add("foo");
    binder.when(b).is(true).add("foo").to(list);
    assertThat(list, not(hasItem("foo")));
  }

}
