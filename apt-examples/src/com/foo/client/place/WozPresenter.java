package com.foo.client.place;

import org.tessell.GenPlace;
import org.tessell.place.PlaceRequest;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

public class WozPresenter {

	@GenPlace(name = "woz", async = false)
	public static void onRequest(EventBus bus, PlaceRequest request) {
	}

	public static void test() {
		// this isn't async, so we don't need the FailureCallback
		new WozPlace(new SimpleEventBus());
	}

}
