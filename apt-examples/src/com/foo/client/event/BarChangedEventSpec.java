package com.foo.client.event;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent(methodName = "onBarDone")
public class BarChangedEventSpec {
	@Param(1)
	Integer fooId;
}
