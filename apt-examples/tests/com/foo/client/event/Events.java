package com.foo.client.event;

import com.google.gwt.event.shared.HandlerManager;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class Events {

	public static class Foo implements FooChangedHandler {
		@Override
		public void onFooChanged(FooChangedEvent event) {
		}
	}

	public static class Bar implements BarChangedHandler {
		@Override
		public void onBarDone(BarChangedEvent event) {
		}
	}

	public static void main(String[] args) {
		EventBus b = new SimpleEventBus();
		b.addHandler(GenericChangedEvent.getType(), new GenericChangedHandler<String>() {
			@Override
			public void onGenericChanged(GenericChangedEvent<String> event) {
				System.out.println(event.getT());
			}
		});

		System.out.println(new BarChangedEvent(1));
		System.out.println(new BarChangedEvent(null));
		System.out.println(new BoundsChangedEvent<Number, Number>(1, 2));

		System.out.println(new BarChangedEvent(1).equals(new BarChangedEvent(1)));
		System.out.println(new BarChangedEvent(1).equals(new BarChangedEvent(2)));

		BarChangedEvent.fire(b, 1);
		BoundsChangedEvent.fire(b, 1, 2);

		// EventBus bus = new DefaultEventBus();
		// BarChangedEvent.fire(bus, 1);
		// BoundsChangedEvent.fire(bus, 1, 2);

		b.fireEvent(new GenericChangedEvent<String>("foo"));
		// t.fireEvent(new GenericChangedEvent<Integer>(1));

    // setting gwtEvent = true allows using the old GwtEvent
		HandlerManager h = new HandlerManager(null);
		OldSchoolEvent.fire(h, 1);
		com.google.gwt.event.shared.EventBus o = new com.google.gwt.event.shared.SimpleEventBus();
		OldSchoolEvent.fire(o, 1);
	}
}
