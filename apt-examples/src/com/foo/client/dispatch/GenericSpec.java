package com.foo.client.dispatch;

import org.tessell.GenDispatch;
import org.tessell.In;
import org.tessell.Out;

@GenDispatch
public class GenericSpec<T, U extends Number> {

	@In(1)
	T t;
	@In(2)
	U u;

	@Out(1)
	T t2;
	@Out(2)
	U u2;

}
