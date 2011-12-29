package com.foo.client.event;

import org.tessell.GenEvent;
import org.tessell.Param;

@GenEvent
public class GenericChangedEventSpec<T> {
	@Param(1)
	T t;
}