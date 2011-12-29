package com.foo.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GreetingServiceAsync {

	void didGreet(AsyncCallback<Boolean> callback);

	void doa(AsyncCallback<Void> callback);

	void fooGreat(String one, int two, AsyncCallback<Void> callback);

	void greetServer(String name, AsyncCallback<String> callback);

}
