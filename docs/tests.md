---
layout: default
title: Tests
---

Tests
=====

As testing is one of the main goals of an MVP architecture, Tessell strives to make testing as easy as possible.

It does this by providing out-of-the-box fake (stub) view objects, for both common GWT widgets (e.g. [StubTextBox][StubTextBox]), and your own UiBinder-based views.

This means you can write tests that:

* Test the presenter/model business logic by poking/observing the (fake) view
* Run quickly in memory
* Avoid mock object setup/verification code

As a short example, [ClientPresenterTest][ClientPresenterTest] from the [gwt-hack](https://github.com/stephenh/gwt-hack) sample project includes a test of what happens on key up:

    @Test
    public void keyUpChangesNameLeft() {
      dto.name = "foo";
      presenter.bind();
      assertThat(view.name().getText(), is("foo"));
      assertThat(view.nameLeft().getText(), is("47 left"));

      view.name().press('b');
      assertThat(view.nameLeft().getText(), is("46 left"));
    }
{: class="brush:java"}

Note how the `view.name().press('b')` call results in:

* `StubTextBox` firing (fake) key down/key press/key up events
* The presenter's bound key up handler being called
* The model's "name" property being updated
* The model's derived "name chars left" property being updated
* The view's bound `nameLeft` element's inner text being updated

This flow of events is typical for rich, event-driven UIs, and the above test shows how Tessell enables testing all of an application's presenter logic, model logic, and view binding end-to-end succinctly, even while using fake, in-memory widgets.

For more information, see either [stubs](stubs.html) for information about the stubs themselves, or the [tutorial](tutorial.html) for how `ClientPresenterTest` fits into the bigger picture.




[ClientPresenterTest]: https://github.com/stephenh/gwt-hack/blob/master/src/test/java/com/bizo/gwthack/client/presenters/ClientPresenterTest.java

[StubTextBox]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/StubTextBox.java

