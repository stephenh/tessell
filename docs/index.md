---
layout: default
title: gwt-mpv
---

gwt-mpv
=======

gwt-mpv is an application framework for writing GWT applications. It enables you to:

* Write GWT applications with lots of really fast unit tests
* Follow a Model View Presenter (MVP`*`) architecture without boilerplate

Features
--------

gwt-mpv has three main features:

* [View generation](./viewgeneration.html) leverages `ui.xml` files to generate all of the interface and implementation boilerplate needed for fast, DOM-decoupled unit tests
* [Rich models](./richmodels.html) to make your application more declarative and less inner-class heavy
* [Dispatch](./dispatch.html)-style server/client AJAX communication

Download
--------

The latest release of gwt-mpv can be found in the joist maven repo:

* [http://repo.joist.ws](http://repo.joist.ws/)

E.g. see the [gwt-mpv-user](http://repo.joist.ws/org/gwtmpv/gwt-mpv-user/) directory.

Changes
-------

Following the [gwt-mpv](https://github.com/stephenh/gwt-mpv) github project is the best way to stay in the loop on changes.

Community
---------

* [gwt-mpv mailing list](https://groups.google.com/forum/?hl=en#!forum/gwtmpv)

Credits
-------

* [Bizo](http://www.bizo.com) for their initial and continued support of `gwt-mpv`
* [gwt-pectin](http://code.google.com/p/gwt-pectin/) inspired gwt-mpv's binding DSL, which was previously very ugly
* [gwt-dispatch](http://code.google.com/p/gwt-dispatch/) for the initial implementation of the dispatch pattern from Ray Ryan's I/O talk


Foot Notes
----------

`*` Yes, the project name gwt-mpv purposefully reorders the "MVP" acronym because: `gwt-mvp` is too generic, and the view should be the dumbest part of your app, so the "V" should come last.

Also note that Dolphin Smalltalk called it's framework "model presenter view".



