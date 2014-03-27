package org.tessell.model.events;

import java.util.List;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.model.properties.Property;
import org.tessell.util.ListDiff;

@GenEvent(gwtEvent = true)
public class ListChangedEventSpec<E> {
  @Param(1)
  Property<List<E>> property;
  @Param(2)
  List<E> oldValue;
  @Param(3)
  List<E> newValue;
  @Param(4)
  ListDiff<E> diff;
}
