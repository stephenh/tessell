---
layout: default
title: Motivation
---

Motivation
==========

Tessell has three primary motivations:

1. Provide an MVP architecture with as little view boilerplate as possible
2. Provide rich models/data binding to have as little inner-class boilerplate as possible
3. Provide a structured "way to do things" that results in well-built GWT/MVP applications

MVP
---

MVP (model, view, presenter) is a way of building a UI that decouples the UI logic from the UI widgets. There is a lot of documentation on this:

* The Ray Ryan [I/O talk](http://www.youtube.com/watch?v=PDuhR18-EdM) that got it all started in the GWT ecosystem
* Fowler's [GUI Architectures](http://martinfowler.com/eaaDev/uiArchs.html) (unfortunately, unlike most of Fowler's work, I find this one hard to follow)
* Official [writeup](http://code.google.com/webtoolkit/articles/mvp-architecture.html) in the GWT docs

But the short of it is: running tests against real on-screen/browser-based widgets is usually horribly slow, we should abstract them some how.

Contrary to traditional approach from the Ray Ryan talk/GWT docs, Tessell simply makes interfaces for each widget. If you have `TextBox`, you code against `IsTextBox`. At test time, a `StubTextBox` pretends to be your textbox.

Your real UI code goes in a presenter class which, since it only talks to `IsXxx` widget interfaces, you can use the stub widgets to unit test your presenter very quickly.

See [view generation](./viewgeneration.html) for more.

Rich Models
-----------

Tessell places much more emphasis on the *M* in MVP by having rich, client-side models.

The whole point of AJAX/desktop-style apps is to apply non-trivial business/validation logic on the client-side, so it only makes sense to have a rich way of expressing the business model vs. just DTOs.

(This is unlike traditional GWT MVP, which only uses DTOs, essentially ignoring the "M" part of MVP).

A major benefit of rich models is data binding: declaratively tying a property of your model object to a UI widget in as few lines as code as possible.

For example, model-less/DTO-only UI logic typically has to use inner classes to respond to updates:

    view.name().addClickHandler(new OnNameClick());

    private class OnNameClick implements ClickHandler() {
      public void onClick(ClickEvent e) {
        model.setName(view.name().getText());
      }
    }
{: class="brush:java"}

While a rich model with data binding allows the much simpler:

    binder.bind(model.name).to(view.name());
{: class="brush:java"}

Other frameworks using rich models/data binding include:

* Cocoa's key/value pattern
* [SproutCore](http://www.sproutcore.com/), a Cocoa-like Javascript framework
* [gwt-pectin](http://code.google.com/p/gwt-pectin/)

Tessell uses a lot of these same ideas, and basically reimplements them in an MVP-/unit-test-able fashion.

See [rich models](./richmodels.html) for more.

