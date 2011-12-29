package com.foo.client.place;

import org.tessell.GenPlace;
import org.tessell.place.PlaceRequest;
import org.tessell.util.FailureCallback;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

public class ZazPresenter {

	@GenPlace(name = "zaz")
	public static void onRequest(EventBus bus, PlaceRequest request) {
	}

	public static void test() {
		FailureCallback failureCallback = null;
		new ZazPlace(new SimpleEventBus(), failureCallback);
	}

}
