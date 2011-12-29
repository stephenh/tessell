package com.foo.client.dispatch.base;

import org.tessell.dispatch.shared.Action;
import org.tessell.dispatch.shared.Result;

public abstract class BaseAction<R extends Result> implements Action<R> {

	public static final String NAME = "com.foo.client.dispatch.base.BaseAction";

	public void inBaseAction() {
	}

}
