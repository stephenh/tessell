package org.tessell.tests.model.properties;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.listProperty;
import static org.tessell.model.properties.NewProperty.stringProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubCheckBox;
import org.tessell.model.dsl.Binder;
import org.tessell.model.events.*;
import org.tessell.model.properties.*;
import org.tessell.model.properties.ListProperty.ElementConverter;
import org.tessell.model.properties.ListProperty.ElementFilter;
import org.tessell.model.properties.ListProperty.ElementMapper;
import org.tessell.model.validation.rules.AbstractRule;
import org.tessell.model.validation.rules.Size;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;
import org.tessell.util.ListDiff;
import org.tessell.util.NaturalComparator;

public class ListPropertyTest {

  private static final Comparator<String> naturalComparator = new NaturalComparator<String>();
  private final SetValue<List<String>> pValue = new SetValue<List<String>>("p");
  private final ListProperty<String> p = new ListProperty<String>(pValue);
  private final CountingAdds<String> adds = new CountingAdds<String>();
  private final CountingRemoves<String> removes = new CountingRemoves<String>();
  private final CountingChanges<List<String>> changes = new CountingChanges<List<String>>();
  private final LastDiff<String> lastDiff = new LastDiff<String>();

  @Before
  public void initialValue() {
    p.setInitialValue(new ArrayList<String>());
    p.addValueAddedHandler(adds);
    p.addValueRemovedHandler(removes);
    p.addPropertyChangedHandler(changes);
    p.addListChangedHandler(lastDiff);
  }

  @Test
  public void addingFiresValueAddedAndChanged() {
    p.add("foo");
    assertThat(adds.count, is(1));
    assertThat(changes.count, is(1));

    p.add("bar");
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(2));
  }

  @Test
  public void addingFiresValueAddedAndChangedWhenUsedViaTheCstr() {
    ListProperty<String> b = new ListProperty<String>(new SetValue<List<String>>("b", new ArrayList<String>()));
    b.addValueAddedHandler(adds);
    b.addPropertyChangedHandler(changes);
    assertThat(adds.count, is(0));
    assertThat(changes.count, is(0));

    b.add("blah");
    assertThat(adds.count, is(1));
    assertThat(changes.count, is(1));
  }

  @Test
  public void addAllFiresValueAddedAndChanged() {
    p.addAll(list("foo", "bar"));
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));
  }

  @Test
  public void addAtIndexFiresValueAddedAndChanged() {
    p.add("foo");
    assertThat(adds.count, is(1));
    assertThat(changes.count, is(1));

    p.add(0, "bar");
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(2));

    assertThat(p.toArrayList(), contains("bar", "foo"));
  }

  @Test
  public void derivedPropertiesAreTouchedOnCreationIfNeeded() {
    p.setTouched(true);
    final IntegerProperty size = p.size();
    assertThat(p.isTouched(), is(true));
    assertThat(size.isTouched(), is(true));
  }

  @Test
  public void addingTouchesDerived() {
    p.setTouched(false);
    final IntegerProperty size = p.size();
    assertThat(p.isTouched(), is(false));
    assertThat(size.isTouched(), is(false));

    p.add("foo");
    assertThat(size.isTouched(), is(true));
  }

  @Test
  public void removeFiresValueRemovedAndChanged() {
    p.add("foo");
    assertThat(changes.count, is(1));
    p.remove("foo");
    assertThat(removes.count, is(1));
    assertThat(changes.count, is(2));
  }

  @Test
  public void removeTouches() {
    assertThat(p.isTouched(), is(false));
    p.remove("a");
    assertThat(p.isTouched(), is(true));
  }

  @Test
  public void removeAll() {
    p.setInitialValue(list("a", "b", "c"));
    assertThat(changes.count, is(1));
    p.removeAll(list("a", "b"));
    assertThat(p.get(), contains("c"));
    assertThat(p.isTouched(), is(true));
    assertThat(changes.count, is(2));
  }

  @Test
  public void clearFiresMultipleRemovesAndOneChange() {
    p.add("foo");
    p.add("bar");
    assertThat(changes.count, is(2));
    p.clear();
    assertThat(removes.count, is(2));
    assertThat(changes.count, is(3));
  }

  @Test
  public void setMakesACopy() {
    ArrayList<String> l = new ArrayList<String>();
    l.add("foo");
    p.set(l);

    l.add("bar");
    assertThat(p.get(), contains("foo"));
  }

  @Test
  public void setFiresADiff() {
    ArrayList<String> l = new ArrayList<String>();
    l.add("foo");
    l.add("bar");
    p.set(l);
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));

    // remove foo, add zaz, keep bar
    l.remove("foo");
    l.add("zaz");
    p.set(l);
    assertThat(adds.count, is(3));
    assertThat(removes.count, is(1));
    assertThat(changes.count, is(2));
  }

  @Test
  public void listChangedEventFires() {
    ArrayList<String> l = new ArrayList<String>();
    l.add("foo");
    p.set(l);
    assertThat(lastDiff.lastDiff.toString(), is("[foo@0]; []; []"));

    p.remove("foo");
    assertThat(lastDiff.lastDiff.toString(), is("[]; []; [foo@0]"));

    p.add("bar");
    assertThat(lastDiff.lastDiff.toString(), is("[bar@0]; []; []"));

    p.add(0, "foo");
    assertThat(lastDiff.lastDiff.toString(), is("[foo@0]; []; []"));
  }

  @Test
  public void setFiresChangeWhenOrderIsDifferent() {
    ArrayList<String> l1 = new ArrayList<String>();
    l1.add("foo");
    l1.add("bar");
    p.set(l1);
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));

    // reorder foo/bar
    ArrayList<String> l2 = new ArrayList<String>();
    l2.add("bar");
    l2.add("foo");
    p.set(l2);
    assertThat(adds.count, is(2));
    assertThat(removes.count, is(0));
    assertThat(changes.count, is(2));
  }

  @Test
  public void setDoesNotFireChangeWhenListIsExactlyTheSame() {
    ArrayList<String> l1 = new ArrayList<String>();
    l1.add("foo");
    l1.add("bar");
    p.set(l1);
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));

    // keep foo/bar
    ArrayList<String> l2 = new ArrayList<String>();
    l2.add("foo");
    l2.add("bar");
    p.set(l2);
    assertThat(adds.count, is(2));
    assertThat(removes.count, is(0));
    assertThat(changes.count, is(1));
  }

  @Test
  public void reassessFiresWhenValueWasNull() {
    SetValue<List<String>> qValue = new SetValue<List<String>>("q", null); // start out null
    ListProperty<String> q = listProperty(qValue);
    q.addValueAddedHandler(adds);
    q.addPropertyChangedHandler(changes);
    qValue.set(list("2", "3"));
    q.reassess();
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));
  }

  @Test
  public void asIntsConvertsExistingValues() {
    p.add("2");
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    assertThat(ints.get(), contains(2));
    assertThat(p.get(), contains("2"));
  }

  @Test
  public void asIntsFailsIfConverterFails() {
    p.add("not an int");
    try {
      p.as(new StringToElementConverter());
      fail();
    } catch (NumberFormatException nfe) {
      // expected
    }
  }

  @Test
  public void asIntsConvertsStringsLater() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.add("3");
    assertThat(ints.get(), contains(3));
    assertThat(p.get(), contains("3"));
  }

  @Test
  public void asIntsRemovesIntsLater() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.add("3");
    p.clear();
    assertThat(ints.get().size(), is(0));
    assertThat(p.get().size(), is(0));
  }

  @Test
  public void asIntsConvertsIntsLater() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.add(3);
    assertThat(p.get(), contains("3"));
    assertThat(ints.get(), contains(3));
  }

  @Test
  public void asIntsRemovesStringsLater() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.add(3);
    ints.clear();
    assertThat(p.get().size(), is(0));
  }

  @Test
  public void asIntsRemovesStringThatAddedAsAnInt() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.add(3);
    p.remove("3");
    assertThat(p.get().size(), is(0));
    assertThat(ints.get().size(), is(0));
  }

  @Test
  public void asIntsRemovesIntThatAddedAsAString() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.add("3");
    ints.remove(3);
    assertThat(p.get().size(), is(0));
    assertThat(ints.get().size(), is(0));
  }

  @Test
  public void asIntsRemovesIntThatAddedInitially() {
    p.add("3");
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.remove(3);
    assertThat(p.get().size(), is(0));
    assertThat(ints.get().size(), is(0));
  }

  @Test
  public void asIntsRemovesMultipleInts() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.add("3");
    p.add("3");
    ints.remove(3);
    ints.remove(3);
    assertThat(p.get().size(), is(0));
    assertThat(ints.get().size(), is(0));
  }

  @Test
  public void asIntsRemovesMultipleStrings() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.add(3);
    ints.add(3);
    p.remove("3");
    p.remove("3");
    assertThat(p.get().size(), is(0));
    assertThat(ints.get().size(), is(0));
  }

  @Test
  public void asIntsUpdatedAfterReassess() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.setValue(list("3"));
    p.reassess();
    assertThat(ints.get(), contains(3));
  }

  @Test
  public void asIntsUpdatedAfterNullReassess() {
    SetValue<List<String>> qValue = new SetValue<List<String>>("q", null); // pass null
    ListProperty<String> q = listProperty(qValue);
    ListProperty<Integer> ints = q.as(new StringToElementConverter());
    qValue.set(list("3"));
    q.reassess();
    assertThat(ints.get(), contains(3));
  }

  @Test
  public void asIntsUpdatesOrderInOriginalList() {
    // given strings 2 and 3
    p.setValue(list("2", "3"));
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));
    // that we convert to ints
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    assertThat(ints.toArrayList(), contains(2, 3));
    // when we change the order of the ints
    ints.set(list(3, 2));
    // then the order change is reflected in the original list of strings
    assertThat(p.toArrayList(), contains("3", "2"));
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(2));
  }

  @Test
  public void asIntsUpdatesOrderInDerivedList() {
    // given strings 2 and 3
    p.setValue(list("2", "3"));
    assertThat(adds.count, is(2));
    assertThat(changes.count, is(1));
    // that we convert to ints
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    assertThat(ints.toArrayList(), contains(2, 3));
    // when we change the order of the strings
    p.set(list("3", "2"));
    // then the order change is reflected in the derivedlist of ints
    assertThat(ints.toArrayList(), contains(3, 2));
  }

  @Test
  public void asIntsShouldMatchTouchedOnSetInitialValueOnOriginalList() {
    ListProperty<String> p = listProperty("p", null);
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.setInitialValue(new ArrayList<String>());
    assertThat(p.isTouched(), is(false));
    assertThat(ints.isTouched(), is(false));
    assertThat(ints.get().isEmpty(), is(true));
  }

  @Test
  public void asIntsShouldMatchTouchedOnSetInitialValueWithNewItems() {
    ListProperty<String> p = listProperty("p", null);
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.setInitialValue(list("1"));
    assertThat(p.isTouched(), is(false));
    assertThat(ints.isTouched(), is(false));
  }

  @Test
  public void asIntsShouldMatchTouchedOnSetInitialValueOnDerivedList() {
    ListProperty<String> p = listProperty("p", null);
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.setInitialValue(new ArrayList<Integer>());
    assertThat(ints.isTouched(), is(false));
    assertThat(p.isTouched(), is(false));
    assertThat(p.get().isEmpty(), is(true));
  }

  @Test
  public void asIntsShouldMatchTouchedOnSetActualValueOnOriginalList() {
    ListProperty<String> p = listProperty("p", null);
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.set(list("1"));
    assertThat(p.isTouched(), is(true));
    assertThat(ints.isTouched(), is(true));
  }

  @Test
  public void asIntsShouldMatchTouchedOnSetActualValueOnDerivedList() {
    ListProperty<String> p = listProperty("p", null);
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    ints.set(list(1));
    assertThat(ints.isTouched(), is(true));
    assertThat(p.isTouched(), is(true));
  }

  @Test
  public void asIntsShouldBeNullIfOriginalListIsNull() {
    ListProperty<String> p = listProperty("p", null);
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    assertThat(ints.get(), is(nullValue()));
  }

  @Test
  public void asIntsShouldMatchTouched() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    assertThat(ints.isTouched(), is(false));
    pValue.set(new ArrayList<String>());
    p.reassess();
    assertThat(p.isTouched(), is(false));
    assertThat(ints.isTouched(), is(false));
  }

  @Test
  public void asIntsInsertPositionInOriginalListIsRespected() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.set(list("3"));
    p.add(0, "2");
    assertThat(ints.get(), contains(2, 3));
  }

  @Test
  public void asIntsAInsertPositionInDerivedListIsRespected() {
    ListProperty<Integer> ints = p.as(new StringToElementConverter());
    p.set(list("3"));
    ints.add(0, 2);
    assertThat(p.get(), contains("2", "3"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void getReturnsUnmodifiableList() {
    p.get().add("1");
  }

  @Test
  public void passedANullListDoesNotNpe() {
    // only constructor and get will work; most other things will fail
    ListProperty<String> l = listProperty("l", null);
    assertThat(l.get(), is(nullValue()));
  }

  @Test
  public void firesMemberChanged() {
    final int[] fires = { 0 };
    ListProperty<DummyModel> models = listProperty("models");
    models.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        fires[0]++;
      }
    });
    DummyModel m1 = new DummyModel();
    models.add(m1);
    assertThat(fires[0], is(1));
    m1.name.set("adsf");
    assertThat(fires[0], is(2));
    models.clear();
    assertThat(fires[0], is(3));
  }

  @Test
  public void firesMemberChangedWhenReassessed() {
    final int[] fires = { 0 };
    SetValue<List<DummyModel>> v = new SetValue<List<DummyModel>>("v");
    v.set(list(new DummyModel("m1")));
    ListProperty<DummyModel> models = new ListProperty<DummyModel>(v);
    models.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        fires[0]++;
      }
    });
    assertThat(fires[0], is(0));
    v.set(list(new DummyModel("m2")));
    models.reassess();
    assertThat(fires[0], is(1));
    v.get().get(0).name.set("newValue");
    assertThat(fires[0], is(2));
  }

  @Test
  public void firesMemberChangedForProperties() {
    final int[] fires = { 0 };
    ListProperty<StringProperty> strings = listProperty("strings");
    strings.addMemberChangedHandler(new MemberChangedHandler() {
      public void onMemberChanged(MemberChangedEvent event) {
        fires[0]++;
      }
    });
    StringProperty s1 = stringProperty("s1");
    strings.add(s1);
    assertThat(fires[0], is(1));
    s1.set("adsf");
    assertThat(fires[0], is(2));
    strings.clear();
    assertThat(fires[0], is(3));
  }

  @Test
  public void derivedValueOfMembers() {
    final ListProperty<DummyModel> models = listProperty("models");
    Property<Integer> startsWithFoo = integerProperty(new DerivedValue<Integer>("startsWithFoo") {
      public Integer get() {
        int i = 0;
        for (DummyModel model : models.get()) {
          if (model.name.get() != null && model.name.get().startsWith("foo")) {
            i++;
          }
        }
        return i;
      }
    });
    assertThat(startsWithFoo.get(), is(0));
    // watch for when foo know's it has changed
    final int[] changed = { 0 };
    startsWithFoo.addPropertyChangedHandler(new PropertyChangedHandler<Integer>() {
      public void onPropertyChanged(PropertyChangedEvent<Integer> event) {
        changed[0]++;
      }
    });
    // adding a non-foo doesn't change it
    models.add(new DummyModel("bar"));
    assertThat(changed[0], is(0));
    // adding a foo does change it
    models.add(new DummyModel("foo"));
    assertThat(changed[0], is(1));
    // changing a non-foo to a foo does change it
    models.get().get(0).name.set("foot");
    assertThat(changed[0], is(2));
    // removing a foo does change it
    models.remove(models.get().get(1));
    assertThat(changed[0], is(3));
  }

  @Test
  public void filteredValues() {
    final ListProperty<DummyModel> models = listProperty("models");
    final ListProperty<DummyModel> foos = models.filter(new ElementFilter<DummyModel>() {
      public boolean matches(DummyModel element) {
        return element.name.get() != null && element.name.get().startsWith("foo");
      }
    });
    assertThat(foos.get().size(), is(0));
    // watch for when foo know's it has changed
    final int[] changed = { 0 };
    foos.addPropertyChangedHandler(new PropertyChangedHandler<List<DummyModel>>() {
      public void onPropertyChanged(PropertyChangedEvent<List<DummyModel>> event) {
        changed[0]++;
      }
    });
    // adding a non-foo doesn't change it
    models.add(new DummyModel("bar"));
    assertThat(changed[0], is(0));
    // adding a foo does change it
    models.add(new DummyModel("foo"));
    assertThat(changed[0], is(1));
    // changing a non-foo to a foo does change it
    models.get().get(0).name.set("foot");
    assertThat(changed[0], is(2));
    // removing a foo does change it
    models.remove(models.get().get(1));
    assertThat(changed[0], is(3));
  }

  @Test
  public void filteredValuesWithNull() {
    final ListProperty<DummyModel> models = listProperty("models", null);
    final ListProperty<DummyModel> foos = models.filter(new ElementFilter<DummyModel>() {
      public boolean matches(DummyModel element) {
        return element.name.get() != null && element.name.get().startsWith("foo");
      }
    });
    assertThat(foos.get().size(), is(0));
    // watch for when foo know's it has changed
    final int[] changed = { 0 };
    foos.addPropertyChangedHandler(new PropertyChangedHandler<List<DummyModel>>() {
      public void onPropertyChanged(PropertyChangedEvent<List<DummyModel>> event) {
        changed[0]++;
      }
    });
    // setting to non-null
    models.set(list(new DummyModel("bar")));
    assertThat(changed[0], is(0));
  }

  @Test
  public void filteredValuesIsReadOnly() {
    final ListProperty<DummyModel> models = listProperty("models");
    final ListProperty<DummyModel> foos = models.filter(new ElementFilter<DummyModel>() {
      public boolean matches(DummyModel element) {
        return element.name.get() != null && element.name.get().startsWith("foo");
      }
    });
    try {
      foos.add(new DummyModel("foo"));
      fail();
    } catch (UnsupportedOperationException uoe) {
      // expected
    }
  }

  @Test
  public void filteredToString() {
    final ListProperty<DummyModel> models = listProperty("models");
    final ListProperty<DummyModel> foos = models.filter(new ElementFilter<DummyModel>() {
      public boolean matches(DummyModel element) {
        return element.name.get() != null && element.name.get().startsWith("foo");
      }
    });
    assertThat(foos.toString(), is("modelsFiltered []"));
  }

  @Test
  public void sizeToString() {
    final ListProperty<DummyModel> models = listProperty("models");
    assertThat(models.size().toString(), is("modelsSize 0"));
  }

  @Test
  public void first() {
    final ListProperty<String> names = listProperty("names");
    final Property<String> first = names.first();
    CountChanges changes = CountChanges.on(first);

    assertThat(first.get(), is(nullValue()));

    names.add("foo");
    assertThat(changes.changes, is(1));
    assertThat(first.get(), is("foo"));

    names.add(0, "bar");
    assertThat(changes.changes, is(2));
    assertThat(first.get(), is("bar"));

    names.add("zaz");
    assertThat(changes.changes, is(2));

    names.clear();
    assertThat(changes.changes, is(3));
    assertThat(first.get(), is(nullValue()));
  }

  @Test
  public void last() {
    final ListProperty<String> names = listProperty("names");
    final Property<String> last = names.last();
    CountChanges changes = CountChanges.on(last);

    assertThat(last.get(), is(nullValue()));

    names.add("foo");
    assertThat(changes.changes, is(1));
    assertThat(last.get(), is("foo"));

    names.add("bar");
    assertThat(changes.changes, is(2));
    assertThat(last.get(), is("bar"));

    names.add(0, "zaz");
    assertThat(changes.changes, is(2));

    names.clear();
    assertThat(changes.changes, is(3));
    assertThat(last.get(), is(nullValue()));
  }

  @Test
  public void testContains() {
    BooleanProperty b = p.contains("s");
    assertThat(b.get(), is(false));
    CountingChanges<Boolean> c = new CountingChanges<Boolean>();
    b.addPropertyChangedHandler(c);
    // null safe
    p.set(null);
    assertThat(c.count, is(0));
    assertThat(b.get(), is(false));
    // fired on add
    p.set(list("a", "s"));
    assertThat(c.count, is(1));
    assertThat(b.get(), is(true));
    // fired on remove
    p.remove("s");
    assertThat(c.count, is(2));
    assertThat(b.get(), is(false));
  }

  @Test
  public void testContainsBound() {
    Binder b = new Binder();
    StubCheckBox c = new StubCheckBox();
    BooleanProperty s = p.contains("s");
    b.bind(s).to(c);
    assertThat(c.getValue(), is(false));
  }

  @Test
  public void testContainsBoundWhenAlreadyTrue() {
    Binder b = new Binder();
    StubCheckBox c = new StubCheckBox();
    p.add("s");
    BooleanProperty s = p.contains("s");
    b.bind(s).to(c);
    assertThat(c.getValue(), is(true));
  }

  @Test
  public void testContainsBoundWhenModelChanges() {
    Binder b = new Binder();
    StubCheckBox c = new StubCheckBox();
    BooleanProperty s = p.contains("s");
    b.bind(s).to(c);
    p.add("s");
    assertThat(c.getValue(), is(true));
  }

  @Test
  public void testContainsBoundWhenUiChanges() {
    Binder b = new Binder();
    StubCheckBox c = new StubCheckBox();
    BooleanProperty s = p.contains("s");
    b.bind(s).to(c);
    c.check();
    assertThat(p.get(), contains("s"));
    c.uncheck();
    assertThat(p.get().isEmpty(), is(true));
  }

  @Test
  public void testContainsAll() {
    p.add("p");
    List<String> some = list("a", "b");
    BooleanProperty b = p.containsAll(some);
    assertThat(b.get(), is(false));

    b.set(true);
    assertThat(p.get(), contains("p", "a", "b"));

    b.set(false);
    assertThat(p.get(), contains("p"));

    p.add("a");
    p.add("b");
    assertThat(b.get(), is(true));

    p.remove("a");
    assertThat(b.get(), is(false));

    b.set(true);
    assertThat(p.get(), contains("p", "b", "a"));
  }

  @Test
  public void testIsValue() {
    final ListProperty<String> s = listProperty("s");
    final Property<Boolean> b = s.is(list("a", "b"));

    assertThat(b.getValue(), is(false));

    s.set(list("a", "b"));
    assertThat(b.getValue(), is(true));

    s.set(list("a", "b", "b"));
    assertThat(b.getValue(), is(false));

    s.set(list("a", "c"));
    assertThat(b.getValue(), is(false));

    s.set(list("b", "a"));
    assertThat(b.getValue(), is(true));

    b.setValue(true);
    assertThat(s.get(), contains("a", "b"));

    b.setValue(false);
    assertThat(s.get().size(), is(0));
  }

  @Test
  public void testIsOther() {
    final ListProperty<String> s1 = listProperty("s1", list("a", "b"));
    final ListProperty<String> s2 = listProperty("s2", list("a", "a"));
    final Property<Boolean> b = s1.is(s2);

    assertThat(b.getValue(), is(false));

    s1.set(list("a", "a"));
    assertThat(b.getValue(), is(true));

    s1.set(list("a"));
    assertThat(b.getValue(), is(false));

    b.setValue(true);
    assertThat(s1.get(), contains("a", "a"));

    b.setValue(false);
    assertThat(s1.get().size(), is(0));
  }

  @Test
  public void testSizeRule() {
    final ListProperty<String> s1 = listProperty("s1", list("a"));
    s1.addRule(new Size<String>("s1 is not the right size", 2, 2));
    assertThat(s1.isValid(), is(false));

    s1.add("b");
    assertThat(s1.isValid(), is(true));

    s1.add("c");
    assertThat(s1.isValid(), is(false));
  }

  @Test
  public void testIsFirst() {
    final BooleanProperty bIsFirst = p.isFirst("b");
    assertThat(bIsFirst.get(), is(false));

    p.add("b");
    assertThat(bIsFirst.get(), is(true));

    p.add(0, "a");
    assertThat(bIsFirst.get(), is(false));

    p.set(null);
    assertThat(bIsFirst.get(), is(false));
  }

  @Test
  public void testIsLast() {
    final BooleanProperty bIsLast = p.isLast("b");
    assertThat(bIsLast.get(), is(false));

    p.add("b");
    assertThat(bIsLast.get(), is(true));

    p.add("c");
    assertThat(bIsLast.get(), is(false));

    p.set(null);
    assertThat(bIsLast.get(), is(false));
  }

  @Test
  public void testMoveUp() {
    // ignore invalid elements
    p.moveUp("a");

    // ignore moving 1st element up
    p.add("a");
    p.moveUp("a");

    // actually move up
    p.add("b");
    assertThat(p.get(), contains("a", "b"));
    p.moveUp("b");
    assertThat(p.get(), contains("b", "a"));
  }

  @Test
  public void testMoveDown() {
    // ignore invalid elements
    p.moveDown("a");

    // ignore moving last element down
    p.add("a");
    p.moveDown("a");

    // actually move down
    p.add("b");
    assertThat(p.get(), contains("a", "b"));
    p.moveDown("a");
    assertThat(p.get(), contains("b", "a"));
  }

  @Test
  public void testIndexOf() {
    IntegerProperty index = p.indexOf("a");
    assertThat(index.get(), is(-1));

    p.add("a");
    assertThat(index.get(), is(0));

    p.add(0, "b");
    assertThat(index.get(), is(1));

    p.set(null);
    assertThat(index.get(), is(-1));
  }

  @Test
  public void testSort() {
    p.set(list("c", "b"));
    p.sort(naturalComparator);
    assertThat(p.get(), contains("b", "c"));
  }

  @Test
  public void testSortIsNotPersistent() {
    p.set(list("c", "b"));
    p.sort(naturalComparator);
    assertThat(p.get(), contains("b", "c"));
    p.add("a");
    assertThat(p.get(), contains("b", "c", "a"));
  }

  @Test
  public void testSortReversesOnMultipleInvocations() {
    p.set(list("c", "b"));
    assertThat(changes.count, is(1));
    assertThat(adds.count, is(2));
    assertThat(removes.count, is(0));

    p.sort(naturalComparator);
    assertThat(p.get(), contains("b", "c"));
    assertThat(changes.count, is(2));
    assertThat(adds.count, is(2));
    assertThat(removes.count, is(0));

    p.sort(naturalComparator);
    assertThat(p.get(), contains("c", "b"));
    assertThat(changes.count, is(3));
    assertThat(adds.count, is(2));
    assertThat(removes.count, is(0));
  }

  @Test
  public void testCursor() {
    // given a list cursor against an empty list
    ListCursor<String> c = p.newCursor();

    // then it starts out with no value
    assertThat(c.value().get(), is(nullValue()));
    assertThat(c.isFirst().get(), is(false));
    assertThat(c.isLast().get(), is(false));

    // when the list finally has a value
    p.add("foo");
    // then the cursor moves to that value
    assertThat(c.value().get(), is("foo"));
    assertThat(c.isFirst().get(), is(true));
    assertThat(c.isLast().get(), is(true));

    // when the list gets another value
    p.add("bar");
    // we stay on the current value
    assertThat(c.value().get(), is("foo"));
    assertThat(c.isFirst().get(), is(true));
    assertThat(c.isLast().get(), is(false));

    // when we move next
    c.moveNext();
    // then the value/properties update
    assertThat(c.value().get(), is("bar"));
    assertThat(c.isFirst().get(), is(false));
    assertThat(c.isLast().get(), is(true));

    // when we move back
    c.moveBack();
    // then the value/properties update
    assertThat(c.value().get(), is("foo"));
    assertThat(c.isFirst().get(), is(true));
    assertThat(c.isLast().get(), is(false));

    // when the client sets the cursor to a valid value
    c.value().set("bar");
    // then the value/properties update
    assertThat(c.value().get(), is("bar"));
    assertThat(c.isFirst().get(), is(false));
    assertThat(c.isLast().get(), is(true));

    // when the client sets the cursor to an invalid value
    c.value().set("invalid");
    // then, currently, we allow that to happen
    assertThat(c.value().get(), is("invalid"));
    assertThat(c.isFirst().get(), is(false));
    assertThat(c.isLast().get(), is(false));

    // when the list is cleared
    p.clear();
    // then the cursor unsets
    assertThat(c.value().get(), is(nullValue()));
    assertThat(c.isFirst().get(), is(false));
    assertThat(c.isLast().get(), is(false));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUnion() {
    ListProperty<String> a = listProperty("a", list("1", "2"));
    ListProperty<String> b = listProperty("b", list("2", "3"));
    ListProperty<String> union = NewProperty.union(a, b);
    assertThat(union.get(), contains("1", "2", "2", "3"));

    CountChanges c = CountChanges.on(union);
    a.remove("1");
    assertThat(c.changes, is(1));
    assertThat(union.get(), contains("2", "2", "3"));

    b.add("4");
    assertThat(c.changes, is(2));
    assertThat(union.get(), contains("2", "2", "3", "4"));
  }

  @Test
  public void testSetComparator() {
    ListProperty<String> a = listProperty("a", list("2", "1"));

    final boolean[] sawNonSorted = { false };
    a.addPropertyChangedHandler(new PropertyChangedHandler<List<String>>() {
      public void onPropertyChanged(PropertyChangedEvent<List<String>> event) {
        List<String> copy = list(event.getNewValue());
        Collections.sort(copy);
        if (!copy.equals(event.getNewValue())) {
          sawNonSorted[0] = true;
        }
      }
    });

    a.setComparator(new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    });
    assertThat(a.get(), contains("1", "2"));
    assertThat(sawNonSorted[0], is(false));

    a.clear();
    assertThat(sawNonSorted[0], is(false));

    a.add("1");
    assertThat(a.get(), contains("1"));
    assertThat(sawNonSorted[0], is(false));

    a.add(0, "2");
    assertThat(a.get(), contains("1", "2"));
    assertThat(sawNonSorted[0], is(false));

    a.add("0");
    assertThat(a.get(), contains("0", "1", "2"));
    assertThat(sawNonSorted[0], is(false));

    a.remove("1");
    assertThat(a.get(), contains("0", "2"));
    assertThat(sawNonSorted[0], is(false));

    a.addAll(list("3", "1"));
    assertThat(a.get(), contains("0", "1", "2", "3"));
    assertThat(sawNonSorted[0], is(false));

    a.set(list("2", "1"));
    assertThat(a.get(), contains("1", "2"));
    assertThat(sawNonSorted[0], is(false));
  }

  @Test
  public void testMap() {
    ListProperty<String> a = listProperty("a");
    ListProperty<Integer> b = a.map(new ElementMapper<String, Integer>() {
      public Integer map(String element) {
        return Integer.valueOf(element);
      }
    });

    assertThat(b.isTouched(), is(false));

    a.setInitialValue(list("1", "2"));
    assertThat(b.get(), contains(1, 2));
    assertThat(b.isTouched(), is(false));

    a.add("3");
    assertThat(b.get(), contains(1, 2, 3));
    assertThat(b.isTouched(), is(true));

    a.remove("1");
    assertThat(b.get(), contains(2, 3));

    a.clear();
    assertThat(b.get().size(), is(0));
  }

  @Test
  public void testAllValidWithProperties() {
    final ListProperty<StringProperty> l = listProperty("l");
    assertThat(l.allValid().get(), is(true));

    StringProperty p = stringProperty("p").req();
    l.add(p);
    assertThat(l.allValid().get(), is(true));

    p.touch();
    assertThat(l.allValid().get(), is(false));

    l.remove(p);
    assertThat(l.allValid().get(), is(true));

    l.addRule(new AbstractRule<List<StringProperty>>("Only one allowed") {
      protected boolean isValid() {
        return property.get().size() == 1;
      }
    });
    assertThat(l.allValid().get(), is(false));

    l.add(p);
    p.set("asdf");
    assertThat(l.allValid().get(), is(true));
  }

  @Test
  public void testAllValidWithModels() {
    final ListProperty<DummyModel> l = listProperty("l");
    assertThat(l.allValid().get(), is(true));

    DummyModel m = new DummyModel();
    m.name.req();
    l.add(m);
    assertThat(l.allValid().get(), is(true));

    m.name.touch();
    assertThat(l.allValid().get(), is(false));

    l.remove(m);
    assertThat(l.allValid().get(), is(true));

    l.addRule(new AbstractRule<List<DummyModel>>("Only one allowed") {
      protected boolean isValid() {
        return property.get().size() == 1;
      }
    });
    assertThat(l.allValid().get(), is(false));

    l.add(m);
    m.name.set("foo");
    assertThat(l.allValid().get(), is(true));
  }

  @Test
  public void testToStringIsAbbreviated() {
    for (int i = 0; i < 20; i++) {
      p.add(Integer.toString(i));
    }
    assertThat(p.toString(), endsWith(", 19]"));
    p.add("20");
    assertThat(p.toString(), endsWith(", 19, ...]"));
    p.set(null);
    assertThat(p.toString(), is("p null"));
    p.set(new ArrayList<String>());
    p.add(null);
    assertThat(p.toString(), is("p [null]"));
  }

  public static class CountingChanges<P> implements PropertyChangedHandler<P> {
    public int count;

    @Override
    public void onPropertyChanged(final PropertyChangedEvent<P> event) {
      count++;
    }
  }

  public static class CountingAdds<P> implements ValueAddedHandler<P> {
    public int count;

    @Override
    public void onValueAdded(final ValueAddedEvent<P> event) {
      count++;
    }
  }

  public static class CountingRemoves<P> implements ValueRemovedHandler<P> {
    public int count;

    @Override
    public void onValueRemoved(final ValueRemovedEvent<P> event) {
      count++;
    }
  }

  public static class LastDiff<P> implements ListChangedHandler<P> {
    public ListDiff<P> lastDiff;

    @Override
    public void onListChanged(ListChangedEvent<P> event) {
      lastDiff = event.getDiff();
    }

  }

  private final class StringToElementConverter implements ElementConverter<String, Integer> {
    @Override
    public Integer to(String element) {
      return element == null ? null : Integer.parseInt(element);
    }

    @Override
    public String from(Integer element) {
      return element == null ? null : element.toString();
    }
  }
}
