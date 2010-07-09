---
layout: default
title: Stubs
---

Stubs
=====

Stubs are not a part of traditional MVP, but they are used extensively in `gwt-mpv`. And since we're generating the `ClientView` implementation anyway, it's easy to generate the `StubClientView` while we're at it.

Testing with stubs rather than mocks makes it easier to succinctly test courser grains of functionality, trending your unit tests more towards functional tests, and making them less brittle in doing so.

Per Fowler's [Mocks aren't Stubs](http://martinfowler.com/articles/mocksArentStubs.html), `gwt-mpv` facilitates a classicist TDD approach where state verification (is the button green now) is favored over behavior verification (did the `setColor(green)` method get called).

