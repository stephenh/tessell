package com.foo.server;

import com.foo.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/** The server side implementation of the RPC service. */
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

	private static final long serialVersionUID = 1L;

	public String greetServer(String input) {
		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	public boolean didGreet() {
		return true;
	}

	public void fooGreat(String one, int two) {
	}

	public void doa() {
	}
}
