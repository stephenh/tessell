package org.tessell.model.events;

import java.util.ArrayList;

import org.gwtmpv.GenEvent;
import org.gwtmpv.Param;
import org.tessell.model.properties.Property;

@GenEvent(gwtEvent = true)
public class ValueRemovedEventSpec<P> {
  @Param(1)
  Property<ArrayList<P>> property;
  @Param(2)
  P value;
}
