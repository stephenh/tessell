package org.tessell.model.events;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent(gwtEvent = true)
public class ValueAddedEventSpec<P> {
  @Param(1)
  P value;
}
