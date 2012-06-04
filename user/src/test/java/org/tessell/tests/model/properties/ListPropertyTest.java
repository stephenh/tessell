package org.tessell.tests.model.properties;

import static joist.util.Copy.list;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.tessell.model.events.PropertyChangedEvent;
import org.tessell.model.events.PropertyChangedHandler;
import org.tessell.model.events.ValueAddedEvent;
import org.tessell.model.events.ValueAddedHandler;
import org.tessell.model.events.ValueRemovedEvent;
import org.tessell.model.events.ValueRemovedHandler;
import org.tessell.model.properties.IntegerProperty;
import org.tessell.model.properties.ListProperty;
import org.tessell.model.values.SetValue;

public class ListPropertyTest {

  final ListProperty<String> p = new ListProperty<String>(new SetValue<ArrayList<String>>("p"));
  final CountingAdds<String> adds = new CountingAdds<String>();
  final CountingRemoves<String> removes = new CountingRemoves<String>();
  final CountingChanges<ArrayList<String>> changes = new CountingChanges<ArrayList<String>>();

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
    ListProperty<String> b = new ListProperty<String>(new SetValue<ArrayList<String>>("b", new ArrayList<String>()));
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
}
