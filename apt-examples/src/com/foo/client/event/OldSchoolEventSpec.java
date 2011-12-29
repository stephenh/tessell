package com.foo.client.event;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent(gwtEvent = true)
public class OldSchoolEventSpec {
	@Param(1)
	Integer fooId;
}
