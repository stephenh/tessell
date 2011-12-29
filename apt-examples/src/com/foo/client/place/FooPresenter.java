package com.foo.client.place;

import org.tessell.GenPlace;
import org.tessell.util.FailureCallback;

public class FooPresenter {

	@GenPlace(name = "foo")
	public static void onRequest() {
	}

	public static void test() {
		FailureCallback failureCallback = null;
		new FooPlace(failureCallback);
	}

}
