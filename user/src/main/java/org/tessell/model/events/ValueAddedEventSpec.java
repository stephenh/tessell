package org.tessell.model.events;

import java.util.List;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.model.properties.Property;

@GenEvent(gwtEvent = true)
public class ValueAddedEventSpec<P> {
  @Param(1)
  Property<List<P>> property;
  @Param(2)
  P value;
  @Param(3)
  int newIndex;
}
