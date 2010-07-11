---
layout: default
title: View Generation
---

View Generation
===============

One of the main ways `gwt-mpv` reduces GWT/MVP-related boilerplate is by using UiBinder `ui.xml` files as the single source of the view and then generating the derivative boilerplate Java code.

This frees the developer from making tedious modifications to multiple files for each UI element.

Traditional MVP
---------------

Let's look at a simple `client.ui.xml` file for clients:

    <ui:UiBinder ...>
      <gwt:HTMLPanel>
        <h2 ui:field="heading">Client</h2>
        <gwt:TextBox ui:field="name"/><br/>
        <gwt:TextBox ui:field="description"/>
        <div><gwt:SubmitButton ui:field="submit" text="Submit"/></div>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class=brush:xml}

If developing [traditional MVP](http://code.google.com/webtoolkit/articles/mvp-architecture.html), you'll have a `ClientPresenter` with a `Display` inner-interface that defines `HasXxx` methods:

    public class ClientPresenter {
      public interface Display {
        HasText heading();

        HasValue<String> name();

        HasValue<String> description();

        HasClickHandlers submit();
      }

      // the rest of your presenter implementation
    }
{: class=brush:java}

Which looks innocent enough. Let's make it a little more real world by adding:

* Besides getting/setting the name and description, we want to listen to their key up events, so we'll add `HasAllKeyHandlers` for both name and description.
* When name or description becomes invalid, we want to add/remove CSS class names. GWT has no `HasStyle` interface, but let's make one with a `WidgetHasStyle` wrapper class, and add `HasStyle` methods for both name and description
* Let's assume our page is showing several client rows (presenters) at the same time, and to integration test the page with Selenium we'll need unique debug ids for each name and description element. So we'll add a `setDebugId` method.

Now it might look like:

    public class ClientPresenter {
      public interface Display {
        HasText heading();

        HasValue<String> name();

        HasAllKeyHandlers nameKeys();

        HasStyle nameStyle();

        HasValue<String> description();

        HasAllKeyHandlers descriptionKeys();

        HasStyle descriptionStyle();

        HasClickHandlers submit();

        void setDebugId(String debugId);
      }

      // the rest of your presenter implementation
    }
{: class=brush:java}

Okay, that looks good. Now let's implement it:

    public class ClientView implements ClientPresenter.Display {
      public static interface MyUiBinder extends UiBinder<HTMLPanel, ClientView> {
      }

      private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

      private final HTMLPanel panel;

      @UiField
      protected Element heading;

      @UiField
      protected TextBox name;

      @UiField
      protected TextBox description;

      @UiField
      protected SubmitButton submit;

      public ClientView() {
        panel = binder.createAndBindUi(this);
      }

      public void setDebugId(String debugId) {
        UIObject.ensureDebugId(heading, debugId + "-heading");
        name.ensureDebugId(debugId + "-name");
        description.ensureDebugId(debugId + "-description");
        submit.ensureDebugId(debugId + "-submit");
      }

      public HasText heading() {
        return heading;
      }

      public HasValue<String> name() {
        return name;
      }

      public HasAllKeyHandlers nameKeys() {
        return name;
      }

      public HasStyle nameStyle() {
        return new WidgetHasStyle(name);
      }

      public HasValue<String> description() {
        return description;
      }

      public HasAllKeyHandlers descriptionKeys() {
        return description;
      }

      public HasStyle descriptionStyle() {
        return new WidgetHasStyle(description);
      }

      public HasClickHandlers submit() {
        return submit;
      }
    }
{: class=brush:java}

This is pretty standard usage of UiBinder and nothing too complex.

But suddenly we went from **8** lines of UiBinder code to **86** lines of derivative Java code to tailor our view for MVP.

`HasXxx` vs. `IsXxx`
====================

One source of cruft are GWT's `HasXxx` interfaces. It is tedious to add a brand new `Display` interface method plus `ClientView` implementation method for each `HasXxx` interface (`HasValue`, `HasAllKeyHandlers`, `HasStyle`, etc.) the presenter requires when they all come from the same widget.

The `HasXxx` interfaces are just a band-aid around the GWT widgets not having their own mockable interfaces--a better solution is to just add widget-specific interfaces and be done with it.

This is what `gwt-mpv` does. For example, `Anchor` has `IsAnchor`, `TextBox` has `IsTextBox`, etc.

This also solves the very annoying problem that many GWT widget methods aren't in `HasXxx` interfaces at all. So you end up having to make them up, e.g. `HasStyle`.

`IsXxx` interfaces also open the door to automation because the programmer no longer has to decide which `HasXxx` interfaces for each widget will be exposed--each widget in the `ui.xml` file is just exposed in the `Display` interface as it's `IsXxx` equivalent.

The presenter can now access as few or as many widget methods as it needs without changing the `Display` and `ClientView` classes.

`gwt-mpv` Generated Views
-------------------------

Let's go back to the `ui.xml` file:

    <ui:UiBinder ...>
      <gwt:HTMLPanel>
        <h2 ui:field="heading">Client</h2>
        <gwt:TextBox ui:field="name"/><br/>
        <gwt:TextBox ui:field="description"/>
        <div><gwt:SubmitButton ui:field="submit" text="Submit"/></div>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class=brush:xml}

Really, all of the key information about the view is right here.

We can see what the `ui:field` bindable fields are (`heading`, `name`, `description`, and`submit`), and what their types are (`Element`, `gwt:TextBox`, `gwt:SubmitButton`).

There is no reason a developer should have to manually copy this information over into 2 separate files (`ClientPresenter.Display` and `ClientView`). [DRY](http://c2.com/cgi/wiki?DontRepeatYourself).

So `gwt-mpv` solves this with code generation. Not GWT compile-time/deferred-binding code generation, but programmer-time, click-the-button, the-IDE-sees-the-output code generation.

By running `ant` or an Eclipse launch target, `gwt-mpv` will parse the `client.ui.xml` file and generate the interface, an implementation, and a stub for testing.

Here is the interface (generated):

    public interface IsClientView extends IsWidget {
        public Widget asWidget();

        public void setDebugId(String baseDebugId);

        public IsElement heading();

        public IsTextBox name();

        public IsTextBox description();

        public IsSubmitButton submit();
    }
{: class=brush:java}

Note that all of `IsXxx` types are interfaces and so are fully mockable/stubable.

And the implementation (generated):

    public class ClientView extends DelegateIsWidget implements IsClientView {

        private static final MyUiBinder binder = GWT.create(MyUiBinder.class);

        @UiField
        Element heading;

        @UiField(provided = true)
        final TextBox name = new org.gwtmpv.widgets.GwtTextBox();

        @UiField(provided = true)
        final TextBox description = new org.gwtmpv.widgets.GwtTextBox();

        @UiField(provided = true)
        final SubmitButton submit = new org.gwtmpv.widgets.GwtSubmitButton();

        public ClientView() {
            setWidget(binder.createAndBindUi(this));
            setDebugId("Client");
        }

        public void setDebugId(String baseDebugId) {
            UIObject.ensureDebugId(heading, baseDebugId + "-heading");
            name.ensureDebugId(baseDebugId + "-name");
            description.ensureDebugId(baseDebugId + "-description");
            submit.ensureDebugId(baseDebugId + "-submit");
        }

        public IsElement heading() {
            return new org.gwtmpv.widgets.GwtElement(heading);
        }

        public IsTextBox name() {
            return (org.gwtmpv.widgets.IsTextBox) name;
        }

        public IsTextBox description() {
            return (org.gwtmpv.widgets.IsTextBox) description;
        }

        public IsSubmitButton submit() {
            return (org.gwtmpv.widgets.IsSubmitButton) submit;
        }

        public static interface MyUiBinder extends UiBinder<HTMLPanel, ClientView> {
        }
    }
{: class=brush:java}

And the stub (generated):

    public class StubClientView extends StubWidget implements IsClientView {
        public final StubIsElement heading = new org.gwtmpv.widgets.StubIsElement();
        public final StubTextBox name = new org.gwtmpv.widgets.StubTextBox();
        public final StubTextBox description = new org.gwtmpv.widgets.StubTextBox();
        public final StubSubmitButton submit = new org.gwtmpv.widgets.StubSubmitButton();

        public StubClientView() {
            setDebugId("Client");
        }

        public void setDebugId(String baseDebugId) {
            heading.ensureDebugId(baseDebugId + "-heading");
            name.ensureDebugId(baseDebugId + "-name");
            description.ensureDebugId(baseDebugId + "-description");
            submit.ensureDebugId(baseDebugId + "-submit");
        }

        public StubIsElement heading() {
            return heading;
        }

        public StubTextBox name() {
            return name;
        }

        public StubTextBox description() {
            return description;
        }

        public StubSubmitButton submit() {
            return submit;
        }
    }
{: class=brush:java}

So, with the stub, it's ~90 lines of code generated from 8 lines of the `client.ui.xml`. An **order of magnitude** decrease in code a programmer has to type out.

(For more on stubs and why they are generated, see [stubs](/stubs.html) and [tests](/tests.html).)

Presenters
----------

After `gwt-mpv` generates the view, it becomes easy for the presenter to use the `IsClientView` interface and the assorted `IsXxx` widget interfaces it exposes.

Here is a `ClientPresenter`:

    public class ClientPresenter extends AbstractPresenter<IsClientView> {
      private Client c = // get client from somewhere

      public ClientPresenter(final IsClientView view) {
        super(view);
      }

      @Override
      protected void onBind() {
        super.onBind();

        // set the debug ids appropriately
        view.setDebugId("Client-" + c.getId());

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
{: class=brush:java}

Notice how:

* The `Display` inner-interface is gone (now generated as `IsClientView`)
* The `view.heading()`, `view.name()`, and `view.description()` return `IsXxx` interfaces that can be used for a variety of interactions--and yet are still fully decoupled from the concrete GWT classes

The Result
----------

With `gwt-mpv` you only maintain two files: your `ui.xml` file and your presenter. The task of percolating `ui.xml` changes throughout various files just to get MVP-testable goodness is now automated.


