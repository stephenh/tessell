package com.foo.client.dispatch.base;

import org.tessell.GenDispatch;
import org.tessell.In;
import org.tessell.Out;

@GenDispatch(baseAction = BaseAction.NAME, baseResult = BaseResult.NAME)
public class BaseFooSpec {

	@In(1)
	Integer integer;
	@Out(1)
	String foo;

	// Shouldn't really put stuff here, but checking compile against base methods
	public void foo() {
		new BaseFooAction(1).inBaseAction();
		new BaseFooResult("1").inBaseResult();
	}

}
