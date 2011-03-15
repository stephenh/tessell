---
layout: default
title: Dispatch
---

Dispatch
========

The dispatch pattern is alternative to the usual GWT-RPC implementation where every remote service is a new servlet. You can learn more about it:

* From Ray Ryan's [Best Practices](http://www.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html) I/O talk
* From [gwt-dispatch](http://code.google.com/p/gwt-dispatch/), the original open source implementation of the pattern

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

What gwt-mpv adds to the dispatch pattern is a large reduction in the boilerplate for making new actions.

Where as traditionally every new action (distinct AJAX call) requires you need to hand-code a `FooAction` (with fields, getters, setters, equals, hashCode) and `FooResult` (also with fields, getters, setters, equals, and hashCode), with gwt-mpv (via it's sister project gwt-mpv-apt), you just specify a "spec" (specification):

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
{: class=brush:java}

And gwt-mpv-apt will generate `FooAction` and `FooResult` DTOs with all of the necessary boilerplate for you.
     
Success Callback
----------------

...

Testing
-------

...


