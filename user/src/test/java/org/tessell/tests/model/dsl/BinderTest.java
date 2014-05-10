package org.tessell.tests.model.dsl;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;
import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.tessell.model.dsl.TakesValues.textOf;
import static org.tessell.model.dsl.WhenConditions.notNull;
import static org.tessell.model.properties.NewProperty.*;
import static org.tessell.testing.TessellMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.tessell.bus.StubEventBus;
import org.tessell.gwt.animation.client.StubAnimation;
import org.tessell.gwt.animation.client.StubAnimations;
import org.tessell.gwt.dom.client.StubClickEvent;
import org.tessell.gwt.dom.client.StubElement;
import org.tessell.gwt.user.client.StubCookies;
import org.tessell.gwt.user.client.ui.*;
import org.tessell.model.dsl.Binder;
import org.tessell.model.dsl.ListBoxAdaptor;
import org.tessell.model.properties.*;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;
import org.tessell.place.PlaceRequest;
import org.tessell.place.events.PlaceRequestEvent;
import org.tessell.tests.model.commands.DummyActiveCommand;
import org.tessell.tests.model.commands.DummyUiCommand;
import org.tessell.tests.model.properties.DummyModel;
import org.tessell.util.cookies.StringCookie;
import org.tessell.widgets.StubTextList;
import org.tessell.widgets.StubWidget;
import org.tessell.widgets.StubWidgetsProvider;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class BinderTest {

  static {
    StubWidgetsProvider.install();
  }

  private final Binder binder = new Binder();
  private final StringProperty s = stringProperty("s");
  private final StubTextBox box = new StubTextBox();
  private final StubTextList errors = new StubTextList();
  private final StubAnchor anchor = new StubAnchor();

  public static enum Color {
    Blue, Green
  };

  @After
  public void tearDown() {
    StubAnimations.clearCapture();
  }

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
  public void propertyToWidgetWithBothUnsetIsANoop() {
    binder.bind(s).to(box);
    assertThat(box.getValue(), is(""));
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void propertyToWidgetUsesWidgetValueIfPropertyIsUninitalized() {
    box.setValue("test");
    binder.bind(s).to(box);
    assertThat(s.get(), is("test"));
  }

  @Test
  public void propertyToWidgetFiresWidgetChange() {
    binder.bind(s).to(box);
    final boolean[] changed = { false };
    box.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(final ValueChangeEvent<String> event) {
        changed[0] = true;
      }
    });
    s.set("test");
    assertThat(changed[0], is(true));
  }

  @Test
  public void propertyToWidgetCanBeUntouched() {
    final SetValue<String> value = new SetValue<String>("value");
    final StringProperty property = stringProperty(value).req();
    binder.bind(property).to(box);
    // start out with a good value
    property.set("good");
    // now change the value behind the scenes (e.g. a new DTO)
    value.set(null);
    // and untouch the property
    property.setTouched(false);
    // we shouldn't have any errors
    assertThat(property.getErrors().size(), is(0));
  }

  @Test
  public void propertyToListBoxUpdatesListBoxWhenPropertyChanges() {
    final StubListBox listBox = new StubListBox();
    final ArrayList<String> values = list(null, "a", "b");

    binder.bind(s).to(listBox, values);
    assertThat(listBox.getSelectedIndex(), is(0));

    s.set("b");
    assertThat(listBox.getSelectedIndex(), is(2));

    s.set(null);
    assertThat(listBox.getSelectedIndex(), is(0));
  }

  @Test
  public void propertyToListBoxUpdatesListBoxWhenPropertyIsAdaptedAndInitiallySet() {
    final StubListBox listBox = new StubListBox();
    final ArrayList<Integer> values = list(1, 2);
    // s starts out null
    assertThat(s.get(), is(nullValue()));
    binder.bind(s).to(listBox, values, new IntegerAdaptor());
    // but is coerced to be a string when we bind
    assertThat(s.get(), is("1"));
    assertThat(s.isTouched(), is(false));
    // and we made sure to set the listBox to the right value
    assertThat(listBox.getSelectedIndex(), is(0));
  }

  @Test
  public void propertyToListBoxDoesNotFailWhenThereAreNoOptions() {
    final StubListBox listBox = new StubListBox();
    final ArrayList<Integer> values = new ArrayList<Integer>();
    // s starts out null
    assertThat(s.get(), is(nullValue()));
    binder.bind(s).to(listBox, values, new IntegerAdaptor());
    // and s is still null
    assertThat(s.get(), is(nullValue()));
    // and so we were unable to select anything
    assertThat(listBox.getSelectedIndex(), is(-1));
  }

  @Test
  public void propertyToListBoxUpdatesPropertyWhenListBoxChanges() {
    final StubListBox listBox = new StubListBox();
    final ArrayList<String> values = list("a", "b");

    binder.bind(s).to(listBox, values);

    listBox.select("b");
    assertThat(s.get(), is("b"));
  }

  @Test
  public void propertyToListBoxHandlesNullValue() {
    final StubListBox listBox = new StubListBox();
    final ArrayList<String> values = list(null, "a", "b");

    binder.bind(s).to(listBox, values);
    assertThat(listBox.getSelectedIndex(), is(0));

    listBox.select("b");
    assertThat(s.get(), is("b"));

    // null gets converted to "" (otherwise it shows up as "null"), so select that
    listBox.select("");
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void propertyToListBoxHandlesEmptyString() {
    final StubListBox listBox = new StubListBox();
    final ArrayList<String> values = list("", "a", "b");

    binder.bind(s).to(listBox, values);
    assertThat(listBox.getSelectedIndex(), is(0));

    listBox.select("b");
    assertThat(s.get(), is("b"));

    listBox.select("");
    assertThat(s.get(), is(""));
  }

  @Test
  public void listPropertyToListBoxChangesListBoxContents() {
    final StubListBox listBox = new StubListBox();
    final ListProperty<String> values = listProperty("values", list("a", "b"));

    binder.bind(s).to(listBox, values);
    assertThat(listBox.getItems(), contains("a", "b"));

    values.set(list("c", "d"));
    assertThat(listBox.getItems(), contains("c", "d"));

    values.set(null);
    assertThat(listBox.getItems().size(), is(0));
  }

  @Test
  public void listPropertyToListBoxUpdatesPropertyWhenListBoxChanges() {
    final StubListBox listBox = new StubListBox();
    final ListProperty<String> values = listProperty("values", list("a", "b"));

    binder.bind(s).to(listBox, values);
    listBox.select("b");
    assertThat(s.get(), is("b"));

    values.set(list("c", "d"));
    listBox.select("c");
    assertThat(s.get(), is("c"));
  }

  @Test
  public void listPropertyToListBoxLeavesSetsPropertyWhenSelectedValueGoesAway() {
    final StubListBox listBox = new StubListBox();
    final ListProperty<Integer> values = listProperty("values", list(1, 2));

    // s starts out null
    assertThat(s.get(), is(nullValue()));

    // and so is set to the 1st option when we bind
    binder.bind(s).to(listBox, values, new IntegerAdaptor());
    assertThat(s.get(), is("1"));

    // when our selected value goes away
    values.set(list(3, 4));

    // then the property isn't changed
    assertThat(s.get(), is("1"));

    // but the list box goes blank
    assertThat(listBox.getSelectedIndex(), is(-1));
  }

  @Test
  public void listPropertyToListBoxDoesNotFailWhenThereAreNoOptions() {
    final StubListBox listBox = new StubListBox();
    final ListProperty<Integer> values = listProperty("values");

    // s starts out null
    assertThat(s.get(), is(nullValue()));
    binder.bind(s).to(listBox, values, new IntegerAdaptor());

    // and s is still null
    assertThat(s.get(), is(nullValue()));

    // and so we were unable to select anything
    assertThat(listBox.getSelectedIndex(), is(-1));

    // when we finally have values
    values.set(list(2, 3));

    // then since we're null, we get assigned to the first
    assertThat(s.get(), is("2"));
  }

  @Test
  public void listPropertyToListBoxDoesNotResetPropertyWhenTheListIsNull() {
    final StubListBox listBox = new StubListBox();

    // s starts out set
    s.set("3");

    // and we bind to a null list property
    final ListProperty<Integer> values = listProperty("values", null);
    binder.bind(s).to(listBox, values, new IntegerAdaptor());

    // then s does not change
    assertThat(s.get(), is("3"));

    // and so the select box is blank
    assertThat(listBox.getSelectedIndex(), is(-1));

    // when we have values that don't include the current value
    values.set(list(1, 2));

    // then s still does not change
    assertThat(s.get(), is("3"));
    assertThat(listBox.getSelectedIndex(), is(-1));

    // when we finally have values that do include the current value
    values.set(list(1, 2, 3));

    // then we can show it in the list box
    assertThat(listBox.getSelectedIndex(), is(2));
  }

  @Test
  public void listPropertyToListBoxHandlesNullValue() {
    final StubListBox listBox = new StubListBox();
    final ListProperty<String> values = listProperty("values", list(null, "a", "b"));

    binder.bind(s).to(listBox, values);
    listBox.select("b");
    assertThat(s.get(), is("b"));

    // null gets converted to "" (otherwise it shows up as "null"), so select that
    listBox.select("");
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void listPropertyToListBoxHandlesEmptyString() {
    final StubListBox listBox = new StubListBox();
    final ListProperty<String> values = listProperty("values", list("", "a", "b"));

    binder.bind(s).to(listBox, values);
    listBox.select("b");
    assertThat(s.get(), is("b"));

    listBox.select("");
    assertThat(s.get(), is(""));
  }

  @Test
  public void propertyToProperty() {
    StringProperty s2 = stringProperty("s2");
    binder.bind(s).to(s2);
    assertThat(s2.get(), is(nullValue()));
    // s -> s2
    s.set("foo");
    assertThat(s2.get(), is("foo"));
    // s2 -> s
    s2.set("bar");
    assertThat(s.get(), is("bar"));
    // manually setting s doesn't cause a loop
    s.set(s2.get());
    assertThat(s.get(), is("bar"));
  }

  @Test
  public void errorsThatAlreadyFiredGetAddedToAnErrorList() {
    s.req().touch();
    assertThat(s.isValid(), is(false));

    binder.bind(s).errorsTo(errors);
    assertThat(errors.getList().size(), is(1));
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
  public void stringPropertyToWidgetSetsMaxLengthViaGenericPropertyBinder() {
    s.max(100);
    // use a Property<String>, so overloading returns the regular
    // PropertyBinder instead of StringPropertyBinder
    Property<String> p = s;
    binder.bind(p).to(box);
    assertThat(box.getMaxLength(), is(100));
  }

  @Test
  public void clickableWidgetToProperty() {
    binder.onClick(box).set(s).to("gotclicked");
    box.click();
    assertThat(s.get(), is("gotclicked"));
  }

  @Test
  public void whenTrueFiresInitialValueWhenTrue() {
    final BooleanProperty b = booleanProperty("b", true);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).set("c").on(w);
    assertThat(w, hasStyle("c"));
  }

  @Test
  public void whenTrueDoesNotFireInitialValueWhenFalse() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).set("c").on(w);
    assertThat(w, not(hasStyle("c")));
  }

  @Test
  public void whenTrueFiresWhenFalseChangesToTrue() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).set("c").on(w);
    b.set(true);
    assertThat(w, hasStyle("c"));
  }

  @Test
  public void whenTrueShowHidesWhenFalse() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).show(w);
    assertThat(w, is(hidden()));
  }

  @Test
  public void whenTrueShowDisplaysWhenTrue() {
    final BooleanProperty b = booleanProperty("b", true);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).show(w);
    assertThat(w, is(shown()));
  }

  @Test
  public void whenTrueShowHidesWhenChangesToFalse() {
    final BooleanProperty b = booleanProperty("b", true);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).show(w);
    b.set(false);
    assertThat(w, is(hidden()));
  }

  @Test
  public void whenTrueAddDoesInitialSet() {
    final BooleanProperty b = booleanProperty("b", true);
    final ArrayList<String> list = new ArrayList<String>();
    binder.when(b).is(true).add("foo").to(list);
    assertThat(list, contains("foo"));
  }

  @Test
  public void whenTrueRemovesFromInitialSet() {
    final BooleanProperty b = booleanProperty("b", false);
    final ArrayList<String> list = new ArrayList<String>();
    list.add("foo");
    binder.when(b).is(true).add("foo").to(list);
    assertThat(list, not(hasItem("foo")));
  }

  @Test
  public void whenValueAddToListInitializesProperty() {
    final EnumProperty<Color> color = enumProperty("color"); // null so we'll do the initial set
    final ArrayList<String> list = new ArrayList<String>();
    list.add("foo");
    binder.when(color).is(Color.Blue).add("foo").to(list);
    assertThat(color.get(), is(Color.Blue));
    assertThat(color.isTouched(), is(false));
    assertThat(list, contains("foo"));
  }

  @Test
  public void whenValueAddToListDoesNotInitializeProperty() {
    final EnumProperty<Color> color = enumProperty("color");
    final ArrayList<String> list = new ArrayList<String>();
    // foo isn't in the list, so we leave color alone
    binder.when(color).is(Color.Blue).add("foo").to(list);
    assertThat(color.get(), is(nullValue()));
    assertThat(color.isTouched(), is(false));
  }

  @Test
  public void whenValueRemoveFromListInitializesProperty() {
    final EnumProperty<Color> color = enumProperty("color");
    final ArrayList<String> list = new ArrayList<String>();
    binder.when(color).is(Color.Blue).remove("foo").from(list);
    assertThat(color.get(), is(Color.Blue));
    assertThat(color.isTouched(), is(false));
  }

  @Test
  public void whenValueAddToListPropertyInitializesProperty() {
    final EnumProperty<Color> color = enumProperty("color");
    final ListProperty<String> list = listProperty("list");
    list.add("foo");
    binder.when(color).is(Color.Blue).add("foo").to(list);
    assertThat(color.get(), is(Color.Blue));
    assertThat(color.isTouched(), is(false));
  }

  @Test
  public void whenValueRemoveFromListPropertyInitializesProperty() {
    final EnumProperty<Color> color = enumProperty("color");
    final ListProperty<String> list = listProperty("list");
    binder.when(color).is(Color.Blue).remove("foo").from(list);
    assertThat(color.get(), is(Color.Blue));
    assertThat(color.isTouched(), is(false));
  }

  @Test
  public void enhanceIgnoresTabKeyUpEvent() {
    final StubTextBox b = new StubTextBox();
    final StringProperty s = stringProperty("s");
    binder.bind(s).to(b);
    binder.enhance(b);
    b.keyUp(KEY_TAB);
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void whenTrueEnableLeavesEnabled() {
    final BooleanProperty b = booleanProperty("b", true);
    final StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).enable(w);
    assertThat(w.isEnabled(), is(true));
  }

  @Test
  public void whenTrueEnableIsFalseThenSetsDisabled() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).enable(w);
    assertThat(w.isEnabled(), is(false));
  }

  @Test
  public void whenTrueEnableChangesToFalseThenSetsDisabled() {
    final BooleanProperty b = booleanProperty("b", true);
    final StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).enable(w);
    b.set(false);
    assertThat(w.isEnabled(), is(false));
  }

  @Test
  public void whenTrueDisabledChangesToDisabled() {
    final BooleanProperty b = booleanProperty("b", true);
    final StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).disable(w);
    assertThat(w.isEnabled(), is(false));
  }

  @Test
  public void whenTrueSetAnotherProperty() {
    final BooleanProperty b = booleanProperty("b", false);
    final IntegerProperty i = integerProperty("i", 1);
    binder.when(b).is(true).set(i).to(10);
    b.set(true);
    assertThat(i.get(), is(10));
  }

  @Test
  public void whenAlreadyTrueSetAnotherProperty() {
    final BooleanProperty b = booleanProperty("b", true);
    final IntegerProperty i = integerProperty("i", 1);
    binder.when(b).is(true).set(i).to(10);
    assertThat(i.get(), is(10));
  }

  @Test
  public void whenValueRun() {
    final boolean[] ran = { false };
    final BooleanProperty b = booleanProperty("b", true);
    binder.when(b).is(false).run(new Runnable() {
      public void run() {
        ran[0] = true;
      }
    });
    assertThat(ran[0], is(false));
    b.set(false);
    assertThat(ran[0], is(true));
  }

  @Test
  public void bindEnumCreatesItems() {
    final SetValue<Color> v = new SetValue<Color>("v", Color.Blue);
    final EnumProperty<Color> e = enumProperty(v);

    final StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getItemCount(), is(2));
    assertThat(box.getItemText(0), is("Blue"));
    assertThat(box.getItemText(1), is("Green"));
  }

  @Test
  public void bindEnumSetsInitialValue() {
    final SetValue<Color> v = new SetValue<Color>("v", Color.Blue);
    final EnumProperty<Color> e = enumProperty(v);

    final StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(0));
  }

  @Test
  public void bindEnumAutoSelectsFirstValueIfNull() {
    final SetValue<Color> v = new SetValue<Color>("v", null);
    final EnumProperty<Color> e = enumProperty(v);

    final StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(0));
    assertThat(v.get(), is(Color.Blue));
  }

  @Test
  public void bindEnumSetsInitialValueToOtherValue() {
    final SetValue<Color> v = new SetValue<Color>("v", Color.Green);
    final EnumProperty<Color> e = enumProperty(v);

    final StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(1));
  }

  @Test
  public void bindEnumSetsValueOnChange() {
    final SetValue<Color> v = new SetValue<Color>("v", Color.Green);
    final EnumProperty<Color> e = enumProperty(v);

    final StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    box.select("Blue");
    assertThat(v.get(), is(Color.Blue));
  }

  @Test
  public void bindEnumUpdatesListBoxOnPropertyChange() {
    final SetValue<Color> v = new SetValue<Color>("v", Color.Green);
    final EnumProperty<Color> e = enumProperty(v);

    final StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(1));
    e.set(Color.Blue);
    assertThat(box.getSelectedIndex(), is(0));
  }

  @Test
  public void emptyStringIsTreatedAsNull() {
    s.set("a");
    binder.bind(s).to(box);
    box.setValue("", true);
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void propertyToCookie() {
    final StubCookies cookies = new StubCookies();
    final StringCookie c = new StringCookie(cookies, "c");
    binder.bind(s).to(c);
    assertThat(s.get(), is(nullValue()));
    assertThat(cookies.get("c"), is(nullValue()));

    s.set("foo");
    assertThat(cookies.get("c"), is("foo"));
  }

  @Test
  public void propertyToCookieGetsInitialCookieValue() {
    final StubCookies cookies = new StubCookies();
    final StringCookie c = new StringCookie(cookies, "c");
    cookies.set("c", "foo");
    binder.bind(s).to(c);
    assertThat(s.get(), is("foo"));
    assertThat(s.isTouched(), is(false));
    assertThat(c.getValue(), is("foo"));
  }

  @Test
  public void propertyToCookieGetsInitialCookieValueUnlessAlreadyTouched() {
    final StubCookies cookies = new StubCookies();
    final StringCookie c = new StringCookie(cookies, "c");
    cookies.set("c", "foo");
    s.touch();
    binder.bind(s).to(c);
    assertThat(s.get(), is(nullValue()));
    assertThat(c.getValue(), is(nullValue()));
  }

  @Test
  public void propertyToCookieGetsInitialCookieValueUnlessAlreadySet() {
    final StubCookies cookies = new StubCookies();
    final StringCookie c = new StringCookie(cookies, "c");
    cookies.set("c", "foo");
    s.setValue("bar");
    s.setTouched(false); // make sure to untouch
    binder.bind(s).to(c);
    assertThat(s.get(), is("bar"));
    assertThat(c.getValue(), is("bar"));
  }

  @Test
  public void derivedValueToIsElement() {
    final StringProperty p = stringProperty(new DerivedValue<String>("p") {
      public String get() {
        return null;
      }
    });
    final StubElement e = new StubElement();
    // should skip the "set null property logic", otherwise will fail
    binder.bind(p).to(textOf(e));
  }

  @Test
  public void whenIsNull() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(notNull()).set("c").on(w);
    assertThat(w, hasStyle("c"));
    b.set(null);
    assertThat(w, not(hasStyle("c")));
  }

  @Test
  public void whenSetToTakesValues() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubElement e = new StubElement();
    binder.when(b).is(true).set(textOf(e)).to("t");
    b.set(true);
    assertThat(e.getInnerText(), is("t"));
  }

  @Test
  public void whenSetToTakesValuesDoesNotTouchOnInitialEvaluation() {
    final BooleanProperty b = booleanProperty("b", true);
    final StringProperty s = stringProperty("s");
    binder.when(b).is(true).set(s).to("t");
    assertThat(s.get(), is("t"));
    assertThat(s.isTouched(), is(false));
  }

  @Test
  public void whenSetToOrElse() {
    final BooleanProperty b = booleanProperty("b");
    binder.when(b).is(true).set(s).toOrElse("a", "b");
    assertThat(s.get(), is("b"));
    b.set(true);
    assertThat(s.get(), is("a"));
    b.set(false);
    assertThat(s.get(), is("b"));
  }

  @Test
  public void whenSetToHasValue() {
    final BooleanProperty b = booleanProperty("b", false);
    binder.when(b).is(true).set(box).to(s);
    s.set("some new value");
    assertThat(box.getValue(), is(""));
    b.set(true);
    assertThat(box.getValue(), is("some new value"));
  }

  @Test
  public void propertyToHasText() {
    final StubLabel label = new StubLabel();
    final StringProperty b = stringProperty("b", "foo");
    binder.bind(b).to(textOf(label));
    // text is initially set
    assertThat(label.getText(), is("foo"));
    // and updated on change
    b.set("bar");
    assertThat(label.getText(), is("bar"));
  }

  @Test
  public void propertyToStringTrims() {
    binder.bind(s).to(box);
    box.type(" foo bar ");
    assertThat(s.get(), is("foo bar"));
    // to the property the value changed from null to "foo bar", so it updates the text box
    assertThat(box.getValue(), is("foo bar"));
  }

  @Test
  public void propertyToStringTrimsToNull() {
    binder.bind(s).to(box);
    box.type("  ");
    assertThat(s.get(), is(nullValue()));
    // to the property the value is still null, so it doesn't update the text box
    assertThat(box.getValue(), is("  "));
  }

  @Test
  public void commandPreventsEventDefault() {
    final DummyUiCommand command = new DummyUiCommand();
    final StubButton button = new StubButton();
    binder.bind(command).to(button);
    final StubClickEvent click = new StubClickEvent();
    button.fireEvent(click);
    assertThat(click.prevented, is(true));
  }

  @Test
  public void commandDisablesButton() {
    final DummyActiveCommand command = new DummyActiveCommand();
    final StubButton button = new StubButton();
    binder.bind(command).to(button);
    button.click();
    assertThat(button.isEnabled(), is(false));
    command.done();
    assertThat(button.isEnabled(), is(true));
  }

  @Test
  public void commandShowsSpinner() {
    final DummyActiveCommand command = new DummyActiveCommand();
    final StubButton button = new StubButton();
    final StubImage spinner = new StubImage();
    binder.bind(command).to(button).spin(spinner);
    assertThat(spinner, is(invisible()));
    button.click();
    assertThat(spinner, is(visible()));
    command.done();
    assertThat(spinner, is(invisible()));
  }

  @Test
  public void onClick() {
    final StubButton button = new StubButton();
    binder.onClick(button).set(s).to("clicked");
    button.click();
    assertThat(s.get(), is("clicked"));
  }

  @Test
  public void booleanToRadioGroupSetsTheInitialValueWhenNull() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b");
    binder.bind(b).to(b1, b2);
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsTheInitialValueWhenTrue() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b", true);
    binder.bind(b).to(b1, b2);
    assertThat(b1.getValue(), is(true));
    assertThat(b2.getValue(), is(false));
  }

  @Test
  public void booleanToRadioGroupSetsTheInitialValueWhenFalse() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(b1, b2);
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsOnTrueClick() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(b1, b2);
    b1.click();
    assertThat(b.get(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsOnFalse() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b", true);
    binder.bind(b).to(b1, b2);
    b2.click();
    assertThat(b.get(), is(false));
  }

  @Test
  public void booleanToRadioGroupSetsOnUpdateFalse() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b", true);
    binder.bind(b).to(b1, b2);
    b.set(false);
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsOnUpdateTrue() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(b1, b2);
    b.set(true);
    assertThat(b1.getValue(), is(true));
    assertThat(b2.getValue(), is(false));
  }

  @Test
  public void booleanToCheckAndView() {
    final StubCheckBox c1 = new StubCheckBox();
    final StubWidget v1 = new StubWidget();
    final BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(c1, v1);
    assertThat(v1, is(hidden()));
    b.set(true);
    assertThat(v1, is(shown()));
  }

  @Test
  public void stringToKeyUpSetsInitialValue() {
    s.set("a");
    binder.bind(s).toKeyUp(box);
    assertThat(box.getValue(), is("a"));
  }

  @Test
  public void stringToKeyUpUpdatesModelOnKeyUp() {
    binder.bind(s).toKeyUp(box);
    box.press('a');
    assertThat(s.get(), is("a"));
  }

  @Test
  public void stringToKeyUpDoesNotTrimOnPress() {
    binder.bind(s).toKeyUp(box);
    box.keyPress('a');
    box.keyPress(' ');
    box.keyUp(0); // should have a keyUp('a')
    assertThat(s.get(), is("a "));
  }

  @Test
  public void stringToKeyUpDoesTrimEntirelyEmptySpaces() {
    binder.bind(s).toKeyUp(box);
    box.keyUp('a');
    box.keyUp(KeyCodes.KEY_BACKSPACE);
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void stringToKeyUpDoestTrimOnChange() {
    binder.bind(s).toKeyUp(box);
    box.type("a ");
    assertThat(s.get(), is("a"));
  }

  @Test
  public void stringToKeyUpUpdatesModelOnChange() {
    binder.bind(s).toKeyUp(box);
    box.type("a"); // just a change, no key up
    assertThat(s.get(), is("a"));
  }

  @Test
  public void stringToKeyUpUpdatesViewOnChange() {
    binder.bind(s).toKeyUp(box);
    s.set("a");
    assertThat(box.getValue(), is("a"));
  }

  @Test
  public void stringToKeyUpSetsMaxLength() {
    s.max(20);
    binder.bind(s).toKeyUp(box);
    assertThat(box.getMaxLength(), is(20));
  }

  @Test
  public void onClickToggleSetsNoInitialValue() {
    final BooleanProperty b = booleanProperty("b");
    binder.onClick(anchor).toggle(b);
    assertThat(b.get(), is(nullValue()));
  }

  @Test
  public void onClickToggleDoesActuallyToggle() {
    final BooleanProperty b = booleanProperty("b");
    binder.onClick(anchor).toggle(b);
    anchor.click();
    assertThat(b.get(), is(true));
    anchor.click();
    assertThat(b.get(), is(false));
  }

  @Test
  public void onClickTogglePreventsDefault() {
    final BooleanProperty b = booleanProperty("b");
    binder.onClick(anchor).toggle(b);
    final StubClickEvent c = new StubClickEvent();
    anchor.fireEvent(c);
    assertThat(c.prevented, is(true));
  }

  @Test
  public void onClickAdd() {
    ListProperty<String> strings = listProperty("strings");
    binder.onClick(anchor).add("a").to(strings);
    assertThat(strings.get().size(), is(0));
    anchor.click();
    assertThat(strings.get().size(), is(1));
  }

  @Test
  public void onClickRemove() {
    ListProperty<String> strings = listProperty("strings");
    strings.add("a");
    binder.onClick(anchor).remove("a").from(strings);
    assertThat(strings.get().size(), is(1));
    anchor.click();
    assertThat(strings.get().size(), is(0));
  }

  @Test
  public void onClickMoveUp() {
    ListProperty<String> strings = listProperty("strings");
    strings.set(list("a", "b"));
    binder.onClick(anchor).moveUp("b").in(strings);
    anchor.click();
    assertThat(strings.get(), contains("b", "a"));
  }

  @Test
  public void onClickMoveDown() {
    ListProperty<String> strings = listProperty("strings");
    strings.set(list("a", "b"));
    binder.onClick(anchor).moveDown("a").in(strings);
    anchor.click();
    assertThat(strings.get(), contains("b", "a"));
  }

  @Test
  public void onClickFocus() {
    final StubFocusWidget f = new StubFocusWidget();
    binder.onClick(anchor).focus(f);
    anchor.click();
    assertThat(f.isFocused(), is(true));
  }

  @Test
  public void onClickPreventDefault() {
    binder.onClick(box).preventDefault();
    StubClickEvent e = new StubClickEvent();
    box.fireEvent(e);
    assertThat(e.prevented, is(true));
  }

  @Test
  public void onClickExecute() {
    DummyUiCommand c = new DummyUiCommand();
    binder.onClick(box).execute(c);
    box.click();
    assertThat(c.getExecutions(), is(1));
  }

  @Test
  public void onClickRun() {
    final Boolean[] ran = { false };
    binder.onClick(box).execute(new Runnable() {
      public void run() {
        ran[0] = true;
      }
    });
    box.click();
    assertThat(ran[0], is(true));
  }

  @Test
  public void onClickGoTo() {
    final StubEventBus bus = new StubEventBus();
    binder.onClick(box).goTo(bus, new PlaceRequest("dummy"));
    box.click();
    assertThat(bus.getEvent(PlaceRequestEvent.class, 0).getRequest().getName(), is("dummy"));
  }

  @Test
  public void onKeyDown() {
    final StubTextBox b = new StubTextBox();
    binder.onKeyDown(b).set(s).to("asdf");
    b.keyDown('a');
    assertThat(s.get(), is("asdf"));
  }

  @Test
  public void onKeyDownFiltered() {
    final StubTextBox b = new StubTextBox();
    binder.onKeyDown(b, KeyCodes.KEY_ENTER).set(s).to("asdf");
    b.keyDown('a');
    assertThat(s.get(), is(nullValue()));
    b.keyDown(KeyCodes.KEY_ENTER);
    assertThat(s.get(), is("asdf"));
  }

  @Test
  public void onChangeToggleSetsNoInitialValue() {
    final BooleanProperty b = booleanProperty("b");
    binder.onChange(box).toggle(b);
    assertThat(b.get(), is(nullValue()));
  }

  @Test
  public void onChangeToggleDoesActuallyToggle() {
    final BooleanProperty b = booleanProperty("b");
    binder.onChange(box).toggle(b);
    box.type("asdf");
    assertThat(b.get(), is(true));
    box.type("fdas");
    assertThat(b.get(), is(false));
  }

  @Test
  public void onPropertyChange() {
    DummyUiCommand c = new DummyUiCommand();
    binder.onChange(s).execute(c);
    s.set("asdf");
    assertThat(c.getExecutions(), is(1));
  }

  @Test
  public void onMemberChange() {
    DummyUiCommand c = new DummyUiCommand();
    DummyModel m = new DummyModel();
    binder.onMemberChange(m).execute(c);
    m.name.set("asdf");
    assertThat(c.getExecutions(), is(1));
  }

  @Test
  public void booleanToListAddsInitiallyWhenTrue() {
    BooleanProperty b = booleanProperty("b", true);
    List<String> names = new ArrayList<String>();
    binder.bind(b).to(names).has("foo");
    assertThat(names, hasItem("foo"));
  }

  @Test
  public void booleanToListInitiallyRemovesWhenTrue() {
    BooleanProperty b = booleanProperty("b", false);
    List<String> names = new ArrayList<String>();
    names.add("foo");
    binder.bind(b).to(names).has("foo");
    assertThat(names.size(), is(0));
  }

  @Test
  public void booleanToListAddsOnChange() {
    BooleanProperty b = booleanProperty("b", false);
    List<String> names = new ArrayList<String>();
    binder.bind(b).to(names).has("foo");
    assertThat(names.size(), is(0));
    b.set(true);
    assertThat(names, hasItem("foo"));
  }

  @Test
  public void booleanToListRemovesOnChange() {
    BooleanProperty b = booleanProperty("b", true);
    List<String> names = new ArrayList<String>();
    names.add("foo");
    binder.bind(b).to(names).has("foo");
    assertThat(names.size(), is(1));
    b.set(false);
    assertThat(names.size(), is(0));
  }

  @Test
  public void booleanToListInitializePropertyToTrue() {
    BooleanProperty b = booleanProperty("b");
    List<String> names = new ArrayList<String>();
    names.add("foo");
    binder.bind(b).to(names).has("foo");
    assertThat(b.get(), is(true));
    assertThat(b.isTouched(), is(false));
  }

  @Test
  public void booleanToListInitializePropertyToFalse() {
    BooleanProperty b = booleanProperty("b");
    List<String> names = new ArrayList<String>();
    binder.bind(b).to(names).has("foo");
    assertThat(b.get(), is(false));
    assertThat(b.isTouched(), is(false));
  }

  @Test
  public void booleanToListPropertyAddsInitiallyWhenTrue() {
    BooleanProperty b = booleanProperty("b", true);
    ListProperty<String> names = listProperty("names");
    binder.bind(b).to(names).has("foo");
    assertThat(names.get(), hasItem("foo"));
  }

  @Test
  public void booleanToListPropertyInitiallyRemovesWhenTrue() {
    BooleanProperty b = booleanProperty("b", false);
    ListProperty<String> names = listProperty("names");
    names.add("foo");
    binder.bind(b).to(names).has("foo");
    assertThat(names.get().size(), is(0));
  }

  @Test
  public void booleanToListPropertyAddsOnChange() {
    BooleanProperty b = booleanProperty("b", false);
    ListProperty<String> names = listProperty("names");
    binder.bind(b).to(names).has("foo");
    assertThat(names.get().size(), is(0));
    b.set(true);
    assertThat(names.get(), hasItem("foo"));
  }

  @Test
  public void booleanToListPropertyRemovesOnChange() {
    BooleanProperty b = booleanProperty("b", true);
    ListProperty<String> names = listProperty("names");
    names.add("foo");
    binder.bind(b).to(names).has("foo");
    assertThat(names.get().size(), is(1));
    b.set(false);
    assertThat(names.get().size(), is(0));
  }

  @Test
  public void booleanToListPropertyInitializePropertyToTrue() {
    BooleanProperty b = booleanProperty("b");
    ListProperty<String> names = listProperty("names");
    names.add("foo");
    binder.bind(b).to(names).has("foo");
    assertThat(b.get(), is(true));
    assertThat(b.isTouched(), is(false));
  }

  @Test
  public void booleanToListPropertyInitializePropertyToFalse() {
    BooleanProperty b = booleanProperty("b");
    ListProperty<String> names = listProperty("names");
    binder.bind(b).to(names).has("foo");
    assertThat(b.get(), is(false));
    assertThat(b.isTouched(), is(false));
  }

  @Test
  public void booleanToListPropertySetsTrueOnAdd() {
    BooleanProperty b = booleanProperty("b", false);
    ListProperty<String> names = listProperty("names");
    binder.bind(b).to(names).has("foo");
    assertThat(names.get().size(), is(0));
    names.add("foo");
    assertThat(b.get(), is(true));
  }

  @Test
  public void booleanToListPropertySetsFalseOnRemove() {
    BooleanProperty b = booleanProperty("b", true);
    ListProperty<String> names = listProperty("names");
    binder.bind(b).to(names).has("foo");
    assertThat(names.get().size(), is(1));
    names.remove("foo");
    assertThat(b.get(), is(false));
  }

  @Test
  public void whenIsOrElse() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).setOrElse("c", "d").on(w);
    assertThat(w, hasStyle("d"));
    assertThat(w, not(hasStyle("c")));
    b.set(true);
    assertThat(w, hasStyle("c"));
    assertThat(w, not(hasStyle("d")));
  }

  @Test
  public void whenIsAttach() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    final StubFlowPanel p = new StubFlowPanel();

    binder.when(b).is(true).attach(w).to(p);
    assertThat(p.getWidgetCount(), is(0));

    b.set(true);
    assertThat(p.getWidgetCount(), is(1));

    b.set(false);
    assertThat(p.getWidgetCount(), is(0));
  }

  @Test
  public void whenIsFadeInStartsFalse() {
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).fadeIn(w);
    // if condition is false, jump right to display none
    assertThat(w, is(hidden()));
    assertThat(w.getStyle().getOpacity(), is(nullValue()));
  }

  @Test
  public void whenIsFadeInStartsTrue() {
    StubAnimations.captureAnimations();
    final BooleanProperty b = booleanProperty("b", true);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).fadeIn(w);
    // if condition is true, jump right to fading in
    assertThat(w, is(shown()));
    StubAnimations.tickAnimation(0);
    assertThat(w.getStyle().getOpacity(), is("0.0"));
    StubAnimations.tickAnimation(1.0);
    assertThat(w.getStyle().getOpacity(), is("1.0"));
  }

  @Test
  public void whenIsFadeInGoesInAndOut() {
    StubAnimations.captureAnimations();
    final BooleanProperty b = booleanProperty("b", false);
    final StubWidget w = new StubWidget();
    binder.when(b).is(true).fadeIn(w);
    // start fading in
    b.set(true);
    StubAnimation a1 = StubAnimations.currentAnimation();
    a1.tick(0.0);
    assertThat(w.getStyle().getOpacity(), is("0.0"));
    a1.tick(0.5);
    assertThat(w.getStyle().getOpacity(), startsWith("0.4"));
    // when we go to false, then we start fading out
    b.set(false);
    assertThat(a1.isCancelled(), is(true));
    StubAnimation a2 = StubAnimations.currentAnimation();
    a2.tick(0.0);
    assertThat(w.getStyle().getOpacity(), is("1.0"));
    a2.tick(1.0);
    assertThat(w.getStyle().getOpacity(), is("0.0"));
    assertThat(w, is(hidden()));
  }

  @Test
  public void toRadioButtons() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    final StubRadioButton b3 = new StubRadioButton();
    binder.bind(s).to(b1, "b1").and(b2, "b2").and(b3, "b3");
    // s wasn't touched
    assertThat(s.get(), is(nullValue()));
    // and we didn't have any value to know what to set
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(false));
    assertThat(b3.getValue(), is(false));
    // clicking each button works
    b1.click();
    assertThat(s.get(), is("b1"));
    b2.click();
    assertThat(s.get(), is("b2"));
    // when model changes, the view is updated
    s.set("b3");
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(false));
    assertThat(b3.getValue(), is(true));
  }

  @Test
  public void toRadioButtonsWithAnExistingValue() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubRadioButton b2 = new StubRadioButton();
    s.set("b1");
    binder.bind(s).to(b1, "b1").and(b2, "b2");
    // s1 didn't change
    assertThat(s.get(), is("b1"));
    // but we know which button to mark as checked
    assertThat(b1.getValue(), is(true));
    assertThat(b2.getValue(), is(false));
    // clicking each button works
    b1.click();
    assertThat(s.get(), is("b1"));
    b2.click();
    assertThat(s.get(), is("b2"));
  }

  @Test
  public void toRadioButtonsAndView() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubWidget v1 = new StubWidget();
    final StubRadioButton b2 = new StubRadioButton();
    final StubWidget v2 = new StubWidget();
    EnumProperty<Color> color = enumProperty("color", Color.Blue);
    binder.bind(color).to(b1, Color.Blue, v1).and(b2, Color.Green, v2);
    assertThat(v1, is(shown()));
    assertThat(v2, is(hidden()));
    b2.check();
    assertThat(v1, is(hidden()));
    assertThat(v2, is(shown()));
  }

  @Test
  public void toRadioButtonAndView() {
    final StubRadioButton b1 = new StubRadioButton();
    final StubWidget v1 = new StubWidget();
    final StubRadioButton b2 = new StubRadioButton();
    final StubWidget v2 = new StubWidget();
    EnumProperty<Color> color = enumProperty("color", Color.Blue);
    binder.bind(color).to(b1, Color.Blue, v1).and(b2, Color.Green, v2);
    assertThat(v1, is(shown()));
    assertThat(v2, is(hidden()));
    b2.check();
    assertThat(v1, is(hidden()));
    assertThat(v2, is(shown()));
  }

  private final class IntegerAdaptor implements ListBoxAdaptor<String, Integer> {
    public String toDisplay(Integer option) {
      return option.toString();
    }

    @Override
    public String toValue(Integer option) {
      return option.toString();
    }
  }

}
