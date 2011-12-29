---
layout: root
title: Tessell
---

<div id="featurecontainer">
  <div id="maincontainer">
    <h2>Tessell is a GWT application framework</h2>

    <ul>
			<li>Follows a Model View Presenter architecture</li>
      <li>Less boilerplate <small>(10x less LOC than hand-coded MVP)</small></li>
    </ul>

    <div class="leftcol">
      <h2>Features</h2>
      <ul>
        <li><a href="./viewgeneration.html">View generation</a> of the MVP/UiBinder interfaces/implementations that allow for fast, DOM-decoupled unit tests but that suck to code by hand</li>
        <li><a href="./richmodels.html">Rich models</a> to make your application's presenter/business logic more declarative and have less spaghetti/inner class code</li>
        <li><a href="./dispatch.html">Dispatch</a>-style server/client AJAX communication</li>
        <li><a href="./stubs.html">Stubs</a> for awesome, out-of-the-box <a href="./tests.html">tests</a></li>
        <li>Conventions for <a href="forms.html">forms</a>, <a href="rowtable.html">row tables</a>, and <a href="celltable.html">cell tables</a></li>
      </ul>
    </div>

    <div class="rightcol">
      <h2>Download</h2>

      <p>Tessell is available in the <a href="http://repo.joist.ws"><code>repo.joist.ws</code></a> Maven repo:</p>

      <ul>
        <li><code>org.tessell</code> <code>tessell-user</code> 2.0.0</li>
        <li><code>org.tessell</code> <code>tessell-dev</code> 2.0.0</li>
        <li><code>org.tessell</code> <code>tessell-apt</code> 2.0.0</li>
      </ul>

      <p>For integrating Tessell into your project, see <a href="./gettingstarted.html">getting started</a>.</p>
    </div>

  </div>
</div>

<div id="contentcontainer">
  <div id="maincontainer" markdown="1">

Community
---------

* [Tessell mailing list](https://groups.google.com/forum/?#!forum/tessell)
* [todomvc ported to Tessell blog post](http://www.draconianoverlord.com/2011/12/10/todomvc-in-gwt-mpv.html)

Open Source
-----------

Tessell is licensed under the Apache Software License and available on github: [tessell](https://github.com/stephenh/tessell).

Following the github project is also the best way to stay in the loop on changes.

Credits
-------

* [Bizo](http://www.bizo.com) for their initial and continued support
* [gwt-pectin](http://code.google.com/p/gwt-pectin/) inspired the binding DSL, which was previously very ugly
* [gwt-dispatch](http://code.google.com/p/gwt-dispatch/) for the initial implementation of the dispatch pattern from Ray Ryan's I/O talk

  </div>
</div>

