package org.gwtmpv.model.events;

import java.util.ArrayList;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.gwtmpv.model.properties.Property;

@GenEvent
public class ValueAddedEventSpec<P> {
  @Param(1)
  Property<ArrayList<P>> property;
  @Param(2)
  P value;
}
