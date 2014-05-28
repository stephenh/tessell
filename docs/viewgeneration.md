---
layout: default
title: View Generation
---

View Generation
===============

One of the main ways Tessell reduces MVP-related boilerplate is by using UiBinder `ui.xml` files as the single source of the view and then generating the derivative boilerplate Java code.

This frees the developer from making tedious modifications to multiple files for each UI element.

Traditional MVP
---------------

With GWT's [traditional MVP](http://code.google.com/webtoolkit/articles/mvp-architecture.html) approach, let's look at a simple [ClientView.ui.xml][ClientViewUiXml] file for editing "client" objects 

    <ui:UiBinder ...>
      <gwt:HTMLPanel>
        <h2 ui:field="heading">Client</h2>
        <gwt:TextBox ui:field="name"/><br/>
        <gwt:TextBox ui:field="description"/>
        <div><gwt:SubmitButton ui:field="submit" text="Submit"/></div>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class="brush:xml"}

After creating the `ui.xml` file, the programmer is now responsible for creating:

* A `ClientPresenter.Display` interface that exposes the specific `HasText`/etc. characteristic interface that the presenter needs for each UI element
* A `ClientView` class that implements `ClientPresenter.Display` via UiBinder, with one field per `ui:field`.

Very briefly, these two files will look something like:

    public class ClientPresenter {
      public interface Display {
        // repeat for each ui:field
        HasValue<String> name();
      }
      // the rest of your presenter implementation
    }

    public class ClientView implements ClientPresenter.Display {
      public static interface MyUiBinder extends UiBinder<HTMLPanel, ClientView> {
      }

      private static final MyUiBinder binder = GWT.create(MyUiBinder.class);
      private final HTMLPanel panel;

      // repeat for each ui:field
      @UiField
      protected TextBox name;

      public ClientView() {
        panel = binder.createAndBindUi(this);
      }

      // repeat for each ui:field
      public HasValue<String> name() {
        return name;
      }
    }
{: class="brush:java"}

With fields, getters, etc., these two classes can easily add up to **80** lines of boilerplate Java code that the programmer has to maintain for an **8** line `ui.xml` file. This is an **10x** increase in manually-maintained lines of code.

Tessell Views
-------------

Tessell takes the stance that the `ui.xml` file has all of the necessary view information right in the file.

Looking again at the `ui.xml` file:

    <ui:UiBinder ...>
      <gwt:HTMLPanel>
        <h2 ui:field="heading">Client</h2>
        <gwt:TextBox ui:field="name"/><br/>
        <gwt:TextBox ui:field="description"/>
        <div><gwt:SubmitButton ui:field="submit" text="Submit"/></div>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class="brush:xml"}

We can say that any element with a `ui:field` attribute (`heading`, `name`, etc.) should automatically be:

* Available to the presenter in the view interface
* Bound with UiBinder in the view implementation

A developer should not have to manually copy this information over into 2 separate files (`ClientPresenter.Display` and `ClientView`). [DRY](http://c2.com/cgi/wiki?DontRepeatYourself).

Tessell solves this by generating both the view interface and implementation. Not with GWT compile-time/deferred-binding code generation, but build-time code generation so that presenters and tests can code against the generated artifacts.

By running `ant` or an Eclipse custom builder (which Eclipse can run automatically on save), Tessell will parse the `ClientView.ui.xml` file and generate the interface, an implementation, and a stub for testing.

So, given the same `ClientView.ui.xml` file, Tessell will generate the view interface:

    public interface IsClientView extends IsWidget {
      // repeat for each ui:field
      public IsTextBox name();
    }
{: class="brush:java"}

Note that all of `IsXxx` types (`IsElement`, `IsTextBox`) are interfaces provided by Tessell and so are fully mockable/stub-able.

Tessell will also generate the UiBinder implementation:

    public class ClientView extends DelegateIsWidget implements IsClientView {
      private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

      // repeat for each ui:field
      @UiField(provided = true)
      final TextBox name = new org.tessell.widgets.GwtTextBox();

      public ClientView() {
        setWidget(binder.createAndBindUi(this));
        ensureDebugId("Client");
      }

      public void ensureDebugId(String baseDebugId) {
        // repeat for each ui:field
        name.ensureDebugId(baseDebugId + "-name");
      }

      // repeated for each ui:field
      public IsTextBox name() {
        return (org.tessell.widgets.IsTextBox) name;
      }

      public static interface MyUiBinder extends UiBinder<HTMLPanel, ClientView> {
      }
    }
{: class="brush:java"}

And, finally, Tessell will generate a view stub for testing:

    public class StubClientView extends StubWidget implements IsClientView {
      // repeat for each ui:field
      private final StubTextBox name = new org.tessell.widgets.StubTextBox();

      public StubClientView() {
        ensureDebugId("Client");
      }

      public void ensureDebugId(String baseDebugId) {
        // repeat for each ui:field
        name.ensureDebugId(baseDebugId + "-name");
      }

      // repeat for each ui:field
      public StubTextBox name() {
        return name;
      }
    }
{: class="brush:java"}

So, with the stub, it's ~90 lines of code generated from 8 lines of the `client.ui.xml`. An **order of magnitude** decrease in code a programmer has to type out.

(For more on stubs and why they are generated, see [stubs](/stubs.html) and [tests](/tests.html).)

Widget Interfaces
-----------------

The primary way Tessell is able to generate views is by eschewing GWT's `HasXxx` characteristic interfaces in favor of `IsXxx` widget interfaces.

With characteristic interfaces, for each widget in the `ui.xml` file, a programmer has to manually decide which of the several `HasXxx` interfaces a widget implements needs to be exposed in the `ClientPresenter.Display` interface. This thwarts any potential automation.

If, instead, each widget has a single corresponding interface, e.g. `TextBox` has an interface `IsTextBox`, that exposes **all** of a widgets characteristics, whether the presenter wants them or not, then the programmer doesn't have to make a decision picking one and we can automate the view generation.

The one wrinkle is that because GWT's `TextBox` class knows nothing about Tessell's `IsTextBox` interface, Tessell includes subclasses for all of the GWT widgets that extend the GWT core widget and merely implement the widget interface. E.g. Tessell has a `class GwtTextBox extends TextBox implements IsTextBox`. This reuses the GWT widget's implementation but satisfies the type system so that a `TextBox` (or really its `GwtTextBox` subclass) can be returned as an `IsTextBox`.


AppViews Interface
------------------

To manage which view implementation (the real UiBinder `ClientView` or testable `StubClientView`) should be used when your presenter needs it, Tessell also generates an `AppViews` class with factory methods to create each of your views.

For example, gwt-hack's `AppViews` class looks something like:

    public class AppViews {
      // repeat for each ui.xml file
      public static IsAppView newAppView() {
        ...
      }
    }
{: class="brush:java"}

When your presenter wants to instantiate its view, it can use the appropriate `AppViews` static method, e.g.:

    public class ClientPresenter extends AbstractPresenter<IsClientView> {
      public ClientPresenter() {
        super(newAppView());
      }
    }
{: class="brush:java"}

Presenters
----------

After Tessell generates the view, it becomes easy for the presenter to use the `IsClientView` interface and the assorted `IsXxx` widget interfaces it exposes.

Here is a `ClientPresenter`:

    public class ClientPresenter extends AbstractPresenter<IsClientView> {

      private Client c = // get client from somewhere

      public ClientPresenter() {
        // will be either the stub or UiBinder as needed
        super(newClientView());
      }

      @Override
      protected void onBind() {
        super.onBind();

        // set the debug ids appropriately
        view.ensureDebugId("Client-" + c.getId());

        // change the heading
        view.heading().setInnerText("Client " + c.getName());

        // set values via IsTextBox.setText
        view.name().setText(c.getName());
        view.description().setText(c.getDescription());

        // listen to key ups
        view.name().addKeyUpHandler(...);
        view.description().addKeyUpHandler(...);

        // when invalid use IsWidget.addStyleName
        view.name().addStyleName("invalid");
        view.description().addStyleName("invalid");

        // when clicked use IsFocusWidget.addClickHandler
        view.submit().addClickHandler(...);
      }
{: class="brush:java"}

Notice how:

* The programmer-maintained `Display` inner-interface is gone (now generated as `IsClientView`)
* The UiBinder-based  view is now generated
* The `view.heading()`, `view.name()`, and `view.description()` return `IsXxx` interfaces that can be used for a variety of interactions--and yet are still fully decoupled from the concrete GWT classes

The Result
----------

With Tessell you only maintain two files:

1. Your `ui.xml` file, and
2. Your presenter.
3. Your presenter's test (okay, so there are 3 files)

The tedious work of percolating `ui.xml` changes throughout various files just to get MVP-testable goodness is automated.

---

### Part I vs. Part II MVP

There are some difference between GWT's [Part I](http://code.google.com/webtoolkit/articles/mvp-architecture.html) and [Part II](http://code.google.com/webtoolkit/articles/mvp-architecture-2.html) MVP architectures, specifically moving from a view interface (Part I) to a presenter callback interface (Part II).

Tessell currently automates a Part I-style architecture. It is not immediately clear how to automate a Part II-style architecture, which does have some nice properties, but so far as not been necessary for the applications Tessell as been used on.

If anyone is interested in using a Tessell-style approach for Part II architectures, feel free to bring it up on the [forums](https://groups.google.com/forum/#!forum/tessell).



[ClientViewUiXml]: https://github.com/stephenh/gwt-hack/blob/master/src/main/java/com/bizo/gwthack/client/views/ClientView.ui.xml
