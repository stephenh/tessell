package org.tessell.tests.model.dsl;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_TAB;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.tessell.model.dsl.TakesValues.textOf;
import static org.tessell.model.dsl.WhenConditions.notNull;
import static org.tessell.model.properties.NewProperty.booleanProperty;
import static org.tessell.model.properties.NewProperty.enumProperty;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;
import static org.tessell.testing.TessellMatchers.hasStyle;
import static org.tessell.testing.TessellMatchers.hidden;
import static org.tessell.testing.TessellMatchers.shown;

import java.util.ArrayList;

import org.junit.Test;
import org.tessell.gwt.dom.client.StubClickEvent;
import org.tessell.gwt.user.client.StubCookies;
import org.tessell.gwt.user.client.ui.*;
import org.tessell.model.dsl.Binder;
import org.tessell.model.properties.BooleanProperty;
import org.tessell.model.properties.EnumProperty;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.StringProperty;
import org.tessell.model.validation.Valid;
import org.tessell.model.values.SetValue;
import org.tessell.tests.model.commands.DummyUiCommand;
import org.tessell.util.cookies.StringCookie;
import org.tessell.widgets.StubTextList;
import org.tessell.widgets.StubWidget;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class BinderTest {

  final Binder binder = new Binder();
  final StringProperty s = stringProperty("s");
  final StubTextBox box = new StubTextBox();
  final StubTextList errors = new StubTextList();

  public static enum Color {
    Blue, Green
  };

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
  public void propertyToWidgetCanBeUntouched() {
    SetValue<String> value = new SetValue<String>("value");
    StringProperty property = stringProperty(value).req();
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
    StubListBox listBox = new StubListBox();
    ArrayList<String> values = new ArrayList<String>();
    values.add(null);
    values.add("a");
    values.add("b");

    binder.bind(s).to(listBox, values);
    s.set("b");
    assertThat(listBox.getSelectedIndex(), is(2));

    s.set(null);
    assertThat(listBox.getSelectedIndex(), is(0));
  }

  @Test
  public void propertyToListBoxUpdatesPropertyWhenListBoxChange() {
    StubListBox listBox = new StubListBox();
    ArrayList<String> values = new ArrayList<String>();
    values.add("a");
    values.add("b");

    binder.bind(s).to(listBox, values);
    listBox.select("b");
    assertThat(s.get(), is("b"));
  }

  @Test
  public void propertyToListBoxHandlesNullValue() {
    StubListBox listBox = new StubListBox();
    ArrayList<String> values = new ArrayList<String>();
    values.add(null);
    values.add("a");
    values.add("b");

    binder.bind(s).to(listBox, values);
    listBox.select("b");
    assertThat(s.get(), is("b"));

    // null gets converted to "" (otherwise it shows up as "null"), so select that
    listBox.select("");
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void propertyToListBoxHandlesEmptyString() {
    StubListBox listBox = new StubListBox();
    ArrayList<String> values = new ArrayList<String>();
    values.add("");
    values.add("a");
    values.add("b");

    binder.bind(s).to(listBox, values);
    listBox.select("b");
    assertThat(s.get(), is("b"));

    listBox.select("");
    assertThat(s.get(), is(""));
  }

  @Test
  public void errorsThatAlreadyFiredGetAddedToAnErrorList() {
    s.req().touch();
    assertThat(s.wasValid(), is(Valid.NO));

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

  @Test
  public void enhanceIgnoresTabKeyUpEvent() {
    StubTextBox b = new StubTextBox();
    StringProperty s = stringProperty("s");
    binder.bind(s).to(b);
    binder.enhance(b);
    b.keyUp(KEY_TAB);
    assertThat(s.get(), is(nullValue()));
  }

  @Test
  public void whenTrueEnableLeavesEnabled() {
    BooleanProperty b = booleanProperty("b", true);
    StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).enable(w);
    assertThat(w.isEnabled(), is(true));
  }

  @Test
  public void whenTrueEnableIsFalseThenSetsDisabled() {
    BooleanProperty b = booleanProperty("b", false);
    StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).enable(w);
    assertThat(w.isEnabled(), is(false));
  }

  @Test
  public void whenTrueEnableChangesToFalseThenSetsDisabled() {
    BooleanProperty b = booleanProperty("b", true);
    StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).enable(w);
    b.set(false);
    assertThat(w.isEnabled(), is(false));
  }

  @Test
  public void whenTrueDisabledChangesToDisabled() {
    BooleanProperty b = booleanProperty("b", true);
    StubFocusWidget w = new StubFocusWidget();
    binder.when(b).is(true).disable(w);
    assertThat(w.isEnabled(), is(false));
  }

  @Test
  public void whenTrueSetAnotherProperty() {
    BooleanProperty b = booleanProperty("b", false);
    IntegerProperty i = integerProperty("i", 1);
    binder.when(b).is(true).set(i).to(10);
    b.set(true);
    assertThat(i.get(), is(10));
  }

  @Test
  public void whenAlreadyTrueSetAnotherProperty() {
    BooleanProperty b = booleanProperty("b", true);
    IntegerProperty i = integerProperty("i", 1);
    binder.when(b).is(true).set(i).to(10);
    assertThat(i.get(), is(10));
  }

  @Test
  public void bindEnumCreatesItems() {
    SetValue<Color> v = new SetValue<Color>("v", Color.Blue);
    EnumProperty<Color> e = enumProperty(v);

    StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getItemCount(), is(2));
    assertThat(box.getItemText(0), is("Blue"));
    assertThat(box.getItemText(1), is("Green"));
  }

  @Test
  public void bindEnumSetsInitialValue() {
    SetValue<Color> v = new SetValue<Color>("v", Color.Blue);
    EnumProperty<Color> e = enumProperty(v);

    StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(0));
  }

  @Test
  public void bindEnumAutoSelectsFirstValueIfNull() {
    SetValue<Color> v = new SetValue<Color>("v", null);
    EnumProperty<Color> e = enumProperty(v);

    StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(0));
    assertThat(v.get(), is(Color.Blue));
  }

  @Test
  public void bindEnumSetsInitialValueToOtherValue() {
    SetValue<Color> v = new SetValue<Color>("v", Color.Green);
    EnumProperty<Color> e = enumProperty(v);

    StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    assertThat(box.getSelectedIndex(), is(1));
  }

  @Test
  public void bindEnumSetsValueOnChange() {
    SetValue<Color> v = new SetValue<Color>("v", Color.Green);
    EnumProperty<Color> e = enumProperty(v);

    StubListBox box = new StubListBox();
    binder.bind(e).to(box, Color.values());
    box.select("Blue");
    assertThat(v.get(), is(Color.Blue));
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
    StubCookies cookies = new StubCookies();
    StringCookie c = new StringCookie(cookies, "c");
    binder.bind(s).to(c);
    assertThat(s.get(), is(nullValue()));
    assertThat(cookies.get("c"), is(nullValue()));

    s.set("foo");
    assertThat(cookies.get("c"), is("foo"));
  }

  @Test
  public void propertyToCookieGetsInitialCookieValue() {
    StubCookies cookies = new StubCookies();
    StringCookie c = new StringCookie(cookies, "c");
    cookies.set("c", "foo");
    binder.bind(s).to(c);
    assertThat(s.get(), is("foo"));
    assertThat(c.getValue(), is("foo"));
  }

  @Test
  public void propertyToCookieGetsInitialCookieValueUnlessAlreadyTouched() {
    StubCookies cookies = new StubCookies();
    StringCookie c = new StringCookie(cookies, "c");
    cookies.set("c", "foo");
    s.touch();
    binder.bind(s).to(c);
    assertThat(s.get(), is(nullValue()));
    assertThat(c.getValue(), is(nullValue()));
  }

  @Test
  public void propertyToCookieGetsInitialCookieValueUnlessAlreadySet() {
    StubCookies cookies = new StubCookies();
    StringCookie c = new StringCookie(cookies, "c");
    cookies.set("c", "foo");
    s.setValue("bar");
    s.setTouched(false); // make sure to untouch
    binder.bind(s).to(c);
    assertThat(s.get(), is("bar"));
    assertThat(c.getValue(), is("bar"));
  }

  @Test
  public void whenIsNull() {
    BooleanProperty b = booleanProperty("b", false);
    StubWidget w = new StubWidget();
    binder.when(b).is(notNull()).set("c").on(w);
    assertThat(w, hasStyle("c"));
    b.set(null);
    assertThat(w, not(hasStyle("c")));
  }

  @Test
  public void propertyToHasText() {
    StubLabel label = new StubLabel();
    StringProperty b = stringProperty("b", "foo");
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
    DummyUiCommand command = new DummyUiCommand();
    StubButton button = new StubButton();
    binder.bind(command).to(button);
    StubClickEvent click = new StubClickEvent();
    button.fireEvent(click);
    assertThat(click.prevented, is(true));
  }

  @Test
  public void onClick() {
    StubButton button = new StubButton();
    binder.onClick(button).set(s).to("clicked");
    button.click();
    assertThat(s.get(), is("clicked"));
  }

  @Test
  public void booleanToRadioGroupSetsTheInitialValueWhenNull() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b");
    binder.bind(b).to(b1, b2);
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsTheInitialValueWhenTrue() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b", true);
    binder.bind(b).to(b1, b2);
    assertThat(b1.getValue(), is(true));
    assertThat(b2.getValue(), is(false));
  }

  @Test
  public void booleanToRadioGroupSetsTheInitialValueWhenFalse() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(b1, b2);
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsOnTrueClick() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(b1, b2);
    b1.click();
    assertThat(b.get(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsOnFalse() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b", true);
    binder.bind(b).to(b1, b2);
    b2.click();
    assertThat(b.get(), is(false));
  }

  @Test
  public void booleanToRadioGroupSetsOnUpdateFalse() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b", true);
    binder.bind(b).to(b1, b2);
    b.set(false);
    assertThat(b1.getValue(), is(false));
    assertThat(b2.getValue(), is(true));
  }

  @Test
  public void booleanToRadioGroupSetsOnUpdateTrue() {
    StubRadioButton b1 = new StubRadioButton();
    StubRadioButton b2 = new StubRadioButton();
    BooleanProperty b = booleanProperty("b", false);
    binder.bind(b).to(b1, b2);
    b.set(true);
    assertThat(b1.getValue(), is(true));
    assertThat(b2.getValue(), is(false));
  }
}
