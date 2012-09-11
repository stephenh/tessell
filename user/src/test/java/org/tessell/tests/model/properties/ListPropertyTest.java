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
import org.tessell.model.events.*;
import org.tessell.model.properties.*;
import org.tessell.model.properties.ListProperty.ElementConverter;
import org.tessell.model.properties.ListProperty.ElementFilter;
import org.tessell.model.values.DerivedValue;
import org.tessell.model.values.SetValue;

public class ListPropertyTest {

  final SetValue<List<String>> pValue = new SetValue<List<String>>("p");
  final ListProperty<String> p = new ListProperty<String>(pValue);
  final CountingAdds<String> adds = new CountingAdds<String>();
  final CountingRemoves<String> removes = new CountingRemoves<String>();
  final CountingChanges<List<String>> changes = new CountingChanges<List<String>>();

  @Before
  public void initialValue() {
    p.set(new ArrayList<String>());
    p.addValueAddedHandler(adds);
    p.addValueRemovedHandler(removes);
    p.addPropertyChangedHandler(changes);
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
  public void derivedPropertiesAreTouchedOnCreationIfNeeded() {
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
