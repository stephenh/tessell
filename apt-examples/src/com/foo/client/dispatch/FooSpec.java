package com.foo.client.dispatch;

import org.tessell.GenDispatch;
import org.tessell.In;
import org.tessell.Out;

@GenDispatch
public class FooSpec {

	@In(1)
	Integer integer;
	@Out(1)
	String foo;
	@Out(2)
	Integer bar;

	// Shouldn't really put stuff here, but checking compile time order
	public void foo() {
		new FooResult("1", 2);
	}

}
