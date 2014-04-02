package org.tessell.tests.model.properties;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.tessell.model.properties.NewProperty.integerProperty;
import static org.tessell.model.properties.NewProperty.listProperty;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.tessell.gwt.user.client.ui.StubCheckBox;
import org.tessell.model.dsl.Binder;
import org.tessell.model.events.*;
import org.tessell.model.properties.*;
import org.tessell.model.properties.ListProperty.ElementConverter;
import org.tessell.model.properties.ListProperty.ElementFilter;
import org.tessell.model.validation.rules.Size;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;
import org.tessell.util.ListDiff;

public class ListPropertyTest {

  final SetValue<List<String>> pValue = new SetValue<List<String>>("p");
  final ListProperty<String> p = new ListProperty<String>(pValue);
  final CountingAdds<String> adds = new CountingAdds<String>();
  final CountingRemoves<String> removes = new CountingRemoves<String>();
  final CountingChanges<List<String>> changes = new CountingChanges<List<String>>();
  final LastDiff<String> lastDiff = new LastDiff<String>();

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
    assertThat(fires[0], is(0));
    m1.name.set("adsf");
    assertThat(fires[0], is(1));
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
