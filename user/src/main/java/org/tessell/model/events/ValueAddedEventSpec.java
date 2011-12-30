package org.tessell.model.events;

import java.util.ArrayList;

import org.tessell.GenEvent;
import org.tessell.Param;
import org.tessell.model.properties.Property;

@GenEvent(gwtEvent = true)
public class ValueAddedEventSpec<P> {
  @Param(1)
  Property<ArrayList<P>> property;
  @Param(2)
  P value;
}
