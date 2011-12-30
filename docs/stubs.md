---
layout: default
title: Stubs
---

Stubs
=====

Stubs are not a part of traditional MVP, but they are used extensively in Tessell.

Their purpose is to provide "dummy browser" behavior in just one place (each stub widget) and not repeated for every view in your application (or in every test in your application as mock specifications).

To do this, each `IsXxx` widget interface (see [View Generation](./viewgeneration.html)) has two implementations:

1. A `GwtXxx` implementation. This is the real GWT widget, so uses `GWT.*`, `DOM.*`, etc. methods that only work in the browser or `GWTTestCase`.

2. A `StubXxx` implementation. This is pure Java so can run in fast unit tests.

Tessell provides `GwtXxx` and `StubXxx` implementations out-of-the-box for most GWT widgets. For an example trio see: [IsTextBox][IsTextBox], [GwtTextBox][GwtTextBox] and [StubTextBox][StubTextBox].

Test Infrastructure For Free
----------------------------

Since Tessell is already generating the `XxxView` implementations for each `ui.xml` file in your project, it also generates a `StubXxxView` for testing.

The real `XxxView` will have `@UiField`-annotated fields of the real GWT widgets used at production time, just like you would code by hand, e.g.:

    @UiField(provided = true)
    final TextBox name = new org.tessell.widgets.GwtTextBox();
{: class=brush:java}

While the `StubXxxView` will simply instantiate the `StubXxx` version of each widget in the `ui.xml` file to use at test time, e.g.:

    final StubTextBox name = new org.tessell.widgets.StubTextBox();
{: class=brush:java}

These "for free" `StubXxxView` classes make testing very easy to do--seeing the [Tests](./tests.html) page for examples.

How Dumb?
---------

When you're testing, the stub widgets attempt to mimic browser behavior, so you can do things like "press a key" or "type":

    public void testKeyPress() {
      view.nameTextBox().press('a');
      view.nameTextBox().type("my name");
    }
{: class=brush:java}

And the `StubTextBox` will, for `press`, fire key down, key press, and key up events and, for `type`, fire value change and blur events.

This behavior is generally "good enough" for 95% of the business logic you're testing.

However, the stubs do not go out of their way to truly simulate a DOM-based browser. Nor do the stubs' behavior correspond to any given browser implementation.

For example, if you set `display:none` on a parent stub widget, a child stub widget will still think it's displayed--there is no cross-widget rendering/etc. logic as this rendering logic would be non-trivial (to write and to run, hence slowing down tests) and, for the far majority of the cases, it isn't needed.

Stubs Are Great
---------------

For more on stubs, see:

* Martin Fowler's article [Mocks aren't Stubs](http://martinfowler.com/articles/mocksArentStubs.html), and
* My own post [Why I Don't Like Mocks](http://www.draconianoverlord.com/2010/07/09/why-i-dont-like-mocks.html)

[IsTextBox]: http://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/IsTextBox.java
[GwtTextBox]: http://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/GwtTextBox.java
[StubTextBox]: http://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/StubTextBox.java

