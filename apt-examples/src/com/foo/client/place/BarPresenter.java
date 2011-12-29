package com.foo.client.place;

import org.tessell.GenPlace;
import org.tessell.place.PlaceRequest;
import org.tessell.util.FailureCallback;

public class BarPresenter {

	@GenPlace(name = "bar", params = { "p1", "p2" })
	public static void onRequest(PlaceRequest request) {
	}

	public static void test() {
		FailureCallback failureCallback = null;
		new BarPlace(failureCallback); // request isn't put into the cstr

		// would be nice to have strongly-typed with methods
		@SuppressWarnings("unused")
		BarPlaceRequest r = BarPlace.newRequest().p1("somename").p2("othername");

		// we can convert a vanilla request to a BarPlaceRequest to get the type-safe accessors
		PlaceRequest vanillaRequest = new PlaceRequest(BarPlace.NAME);
		BarPlaceRequest converted = new BarPlaceRequest(vanillaRequest);
		converted.p1();
		converted.p2();

		// we can access the NAME constant if needed
		System.out.println(BarPlace.NAME);
	}

}
