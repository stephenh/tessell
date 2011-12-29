package com.foo.client.event;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent
public class FooChangedEventSpec {
	@Param(1)
	Integer fooId;
}
