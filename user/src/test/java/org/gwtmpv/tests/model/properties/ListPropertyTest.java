package org.gwtmpv.tests.model.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.gwtmpv.model.events.PropertyChangedEvent;
import org.gwtmpv.model.events.PropertyChangedHandler;
import org.gwtmpv.model.events.ValueAddedEvent;
import org.gwtmpv.model.events.ValueAddedHandler;
import org.gwtmpv.model.events.ValueRemovedEvent;
import org.gwtmpv.model.events.ValueRemovedHandler;
import org.gwtmpv.model.properties.IntegerProperty;
import org.gwtmpv.model.properties.ListProperty;
import org.gwtmpv.model.values.SetValue;
import org.junit.Before;
import org.junit.Test;

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
  }

  @Test
  public void addingTouchesDerived() {
    final IntegerProperty size = p.size();
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
