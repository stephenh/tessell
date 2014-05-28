---
layout: default
title: Dispatch
---

Dispatch
========

The dispatch pattern is alternative to the usual GWT-RPC implementation where every remote service is a new servlet. You can learn more about it:

* From Ray Ryan's [Best Practices](http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html) I/O talk
* From [gwt-dispatch](http://code.google.com/p/gwt-dispatch/), the original open source implementation of the pattern

Tessell's implementation of the dispatch pattern is not terribly unique, other than providing:

* `GenDispatch` annotation for no action/result DTO boilerplate,
* `SuccessCallback` for centralization of error handling, and
* Stub dispatch implementations for unit testing

Overview
--------

In short, instead of having an `FooService`/`FooServiceAsync` for every remote service your application calls:

<img src="/images/dispatch-rpc.png" style="width: 40em;"/>

You use the command pattern to make `FooAction`/`FooResult` DTOs that are submitted via a single `DispatchService` and then "dispatched" appropriately to the right command handler on the server-side:

<img src="/images/dispatch-new.png" style="width: 40em;"/>

While this seems more complex, the advantage is that making new FooAction/FooHandler classes should be easier than making new Foo/FooAsync classes.

Also, since the command pattern is used, your client-side application can pass the commands around to do interesting things (batching, caching, etc.).

Less Boilerplate
----------------

What Tessell adds to the dispatch pattern is a large reduction in the boilerplate for making new actions.

Where as traditionally every new action (distinct AJAX call) requires you need to hand-code a `FooAction` (with fields, getters, setters, equals, hashCode) and `FooResult` (also with fields, getters, setters, equals, and hashCode), with Tessell, you just specify a "spec" (specification):

    @GenDispatch
    public class FooSpec {
      @In(1)
      String inputParam1;
      @In(2)
      String inputParam2;

      @Out(1)
      String outputParam1;
      @Out(2)
      String outputParam2;
    }
{: class="brush:java"}

And `tessell-apt` will generate `FooAction` and `FooResult` DTOs with all of the necessary boilerplate for you.
     
Success Callback
----------------

Every AJAX call may fail, but it's not a good idea to reimplement failure logic at each AJAX call site in your application. To help facilitate this, Tessell adds a [SuccessCallback][SuccessCallback] interface:

    public interface SuccessCallback<T> {
      void onSuccess(T result);
    }
{: class="brush:java"}

Which is just like `AsyncCallback` without the `onFailure` method. So, instead of reimplementing `AsyncCallback.onFailure` each time you make an AJAX call, Tessell's [OutstandingDispatchAysnc][OutstandingDispatchAysnc] accepts `SuccessCallbacks` and will provide a single `onFailure` implementation.

The default `onFailure` implementation fires a `DispatchUnhandledFailureEvent` on the applications `EventBus`, so anyone that is interested (e.g. an error popup listener) can listener for the failure and respond appropriately.

(Note that another common way of achieving this is to have an application-specific subclass of `AsyncCallback` that implements `onFailure`.)

Testing
-------

Tessell provides a [StubDispatchAsync][StubDispatchAsync] that facilitates testing dispatch actions/results in a fairly succinct manner.

Again using [ClientPresenterTest][ClientPresenterTest], testing save looks like:

    @Test
    public void saving() {
      bind();
      view.name().type("bar");
      view.submit().click();

      // ensure we sent the right data
      SaveClientAction sentAction =
        async.getAction(SaveClientAction.class);
      assertThat(sentAction.getClient().name, is("bar"));

      // save on the server is successful
      doSaveClientResult(true);

      // assert that we've moved to #clients
      assertThat(bus, hasPlaceRequests("clients"));
    }

    private void doSaveClientResult(boolean success) {
      async.getCallback(SaveClientAction.class).onSuccess(
        new SaveClientResult(success));
    }
{: class="brush:java"}

Where:

* `view.submit().click()` fires a `SaveClientAction` command (done by the ClientPresenter we're testing)
* We can, if necessary, use `async.getAction(SaveClientAction.class)` to get the last-sent action of that type and make assertions against it, to ensure it has the correct data
* We use `async.getCallback(SaveClientAction.class)` to get the last-sent action's callback and can call either:
  * `onSuccess` with a `SaveClientResult` that came from the server
  * `onFailure` with a `Throwable` to test the failure condition

This allows fairly quick, easy testing of dispatch-style actions and results without a lot of fuss.

Request Factory
---------------

[RequestFactory](http://code.google.com/webtoolkit/doc/latest/DevGuideRequestFactory.html) is another alternative to traditional GWT-RPC.

Nothing in Tessell prevents users from using RequestFactory, in fact it might work quite well, it just has not been actively explored yet.



[SuccessCallback]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/dispatch/client/SuccessCallback.java

[OutstandingDispatchAysnc]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/dispatch/client/util/OutstandingDispatchAsync.java

[StubDispatchAsync]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/dispatch/client/StubDispatchAsync.java

[ClientPresenterTest]: https://github.com/stephenh/gwt-hack/blob/master/src/test/java/com/bizo/gwthack/client/presenters/ClientPresenterTest.java
