---
layout: default
title: Tutorial
---

Tutorial
========

This is a basic tutorial of gwtmpv that walks through the gwt-hack example application.

Setup
-----

To follow along using Eclipse, you can:

* Clone the [gwt-hack](http://github.com/stephenh/gwt-hack) repository
* Install [IvyDE](http://ant.apache.org/ivy/ivyde/) in Eclipse
* Import the project into Eclipse
* Run the `gwt-hack` launch and it should start up

A View
------

We'll start by looking at [ClientView.ui.xml](http://github.com/stephenh/gwt-hack/blob/master/src/main/java/com/bizo/gwthack/client/views/ClientView.ui.xml):

    <ui:UiBinder
      xmlns:ui="urn:ui:com.google.gwt.uibinder"
      xmlns:gwt="urn:import:com.google.gwt.user.client.ui">
      <gwt:HTMLPanel>
        <h2 ui:field="heading">Client</h2>
        <gwt:TextBox ui:field="name" />
        <div>left: <gwt:Label ui:field="nameLeft" /></div>
        <div><gwt:SubmitButton ui:field="submit" text="Submit" /></div>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class=brush:xml}

This is [UiBinder](http://code.google.com/webtoolkit/doc/latest/DevGuideUiBinder.html), GWT client-side templating. At compile-time, GWT turns it into a variety of `DOM.createElement`, `new TextBox`, and `innerHTML` calls.

The `ui:field` attributes are GWT-specific and allow us access to the elements/widgets in the template (e.g. `view.heading().setInnerText(somethingElse)`).

(Note that UiBinder strongly discourages using the `id` attribute, as a given `ui.xml` template may be included multiple times in a single page, resulting in any hard-coded DOM ids conflicting. You can still use `id` attributes in the `ui.xml` file, but only on elements that do not also have a `ui:field` attribute.)

(Tangentially: widgets vs. elements--GWT differentiates between elements (pure DOM objects) and widgets (wrapper objects, e.g. `TextBox`). While elements are lighter weight, widgets have more logic to fix cross-browser idiosyncrasies. For example, in IE6 any GC cycle involving a DOM element leaks memory, so GWT's widgets roll their own `Window`-based event handling system.)

Our goal for this tutorial/page is simple:

* Set the initial value of the `name` text box
* Update the `nameLeft` label as the user types in `name`
* Send the updated client back to the server on submit

Changing the View
-----------------

gwt-mpv's forte is using the `ClientView.ui.xml` file as the source for generating other artifacts that in traditional GWT MVP are hand-maintained. If you edit `ClientView.ui.xml`, make a change, and hit save, you should see the console output:

    <path>/gwt-hack/src/main/java/com/bizo/gwthack/client/views/ClientView.ui.xml
{: class=brush:plain}

The gwt-hack project has a views external tool builder configured to automatically run anytime a file is changed in the `views` package. The builder runs gwt-mpv's `ViewGenerator`, and created:

* `IsClientView`: a pure-Java interface for the `ClientView.ui.xml`
* `GwtClientView`: the GWT/UiBinder version of `ClientView.ui.xml`
* `StubClientView`: a stub version of `ClientView.ui.xml` which fills in all of the elements/widgets with stubs to test against

Given these artifacts are updated automatically, iterating between the view `ui.xml` code and the presenter becomes very quick.

A Presenter
-----------

To implement our view logic, we'll make [ClientPresenter](http://github.com/stephenh/gwt-hack/blob/master/src/main/java/com/bizo/gwthack/client/presenters/ClientPresenter.java).

Going through its source in pieces, we first declare `ClientPresenter` as a presenter that manipulates an `IsClientView` (generated from `ClientView.ui.xml`):

    public class ClientPresenter extends AbstractPresenter<IsClientView> {
    }
{: class=brush:java}

For the constructor, we take the application's `AppRegistry` (add link here) and the `ClientModel` we are to display.

      public ClientPresenter(final AppRegistry registry, final ClientModel client) {
        super(registry.getAppViews().getClientView(), registry);
        this.client = client;
        nameLeft = makeNameleft();
      }
{: class=brush:java}

We also assigned two fields:

* `client` is our model
* `nameLeft` is a custom `StringProperty` for displaying the `X left` text (we'll cover this later)

The main logic of our presenter is in the `onBind` method, which is called when our presenter should be bound to our view:

      @Override
      protected void onBind() {
        super.onBind();
        binder.bind(client.name).to(view.name());
        binder.bind(nameLeft).toTextOf(view.nameLeft());
        binder.bind(saveCommand).to(view.submit());
        binder.enhance(view.name());
      }
{: class=brush:java}

The first `binder.bind` call sets up two-way binding between the `client.name` model and the `view.name()` text box widget. If the model changes, the text box is updated; if the text box changes, the model is updated.

The second `binder.bind` call sets up one-one binding from our custom `nameLeft` model to the text of the `nameLeft` label widget.

The third `binder.bind` call sets up `saveCommand` to be called when `submit` is clicked. `saveCommand` is a `DispatchUiCommand` that sends an action to the server and waits for the result:

      /** Saves the client and returns to the client list. */
      private final UiCommand saveCommand = new DispatchUiCommand<SaveClientAction, SaveClientResult>(async) {
        protected SaveClientAction createAction() {
          return new SaveClientAction(client.getDto());
        }

        protected void onResult(SaveClientResult result) {
          goTo(ClientListPlace.newRequest());
        }
      };
{: class=brush:java}

The `DispatchUiCommand` base class encapsulates the logic that only allows "Submit" to be clicked once, and can optionally disable/enable `submit` as needed (todo).

If save is successful, `goTo(PlaceRequest)` is called, which causes the `#clients` page to be loaded (see place notes here).

If save is unsuccessful, our application-wide error logic will kick in and show an error message to the user, ask them to reload the application, etc.

Finally, coming back to the `nameLeft` field, it is created by the `makeNameLeft` method:

      private StringProperty makeNameLeft() {
        final IntegerProperty remaining = client.name.remaining();
        return new StringProperty(new DerivedValue<String>() {
          public String get() {
            return remaining.get() + " left";
          }
        }).depends(remaining);
      }
{: class=brush:java}

The `DerivedValue` inner class returns the new value of the property and `depends(remaining)` means the `nameLeft` property will be re-evaluated each time the `remaining` property of `name` changes (which in turn changes each time `name` changes).

(`makeNameLeft` needs a sexier, pectin-like DSL.)

A Place
-------

One part of the `ClientPresenter` that I skipped was the static `show` method:

      @GenPlace(value = "client", async = false)
      public static void show(final AppRegistry registry, final AppPresenter app, PlaceRequest request) {
        final String id = request.getParameter("id", null);
        registry.getAsync().execute(new GetClientAction(id), new SuccessCallback<GetClientResult>() {
          public void onSuccess(GetClientResult result) {
            app.show(new ClientPresenter(registry, new ClientModel(result.getClient())));
          }
        });
      }
{: class=brush:java}

Places are part of traditional GWT MPV and denote different bookmarks in an application: `#clients`, `#client;id=4`, etc.

Each token generally has a class it is mapped to, e.g. `#clients` -> `ClientsPlace`, that knows how to kick-start the UI for that page (e.g. create a `ClientPresenter`).

The `@GenPlace` annotation comes from the gwt-mpv-apt annotation processor, and generates most of the `XxxPlace` class boilerplate. This include [GWT.runAsync](http://code.google.com/webtoolkit/doc/latest/DevGuideCodeSplitting.html)-based code splitting.

(link to more place docs)

A Test
------

Finally, the reason we're doing all this, [ClientPresenterTest]():

Again, annotated in pieces:

    public class ClientPresenterTest extends AbstractPresenterTest {
      private final ClientDto dto = new ClientDto();
      private final ClientModel client = new ClientModel(dto);
      private final ClientPresenter p = new ClientPresenter(registry, client);
      private final StubClientView v = (StubClientView) p.getView();
    }
{: class=brush:java}

We assume each of our test methods will use a `dto`, model `client`, presenter `p`, and view `v`, and declare these as fields. Note our view is actually a `StubClientView`.

`AbstractPresenterTest` sets up a few infrastructure things, notably our `StubAppRegistry` (link here) that has stub versions of all of our otherwise GWT dependencies.

    @Test
    public void fillsInFieldsOnBind() {
      dto.name = "foo";
      p.bind();
      assertThat(v.name.getText(), is("foo"));
      assertThat(v.nameLeft.getText(), is("47 left"));
    }
{: class=brush:java}

The first test just asserts that as soon as our presenter is bound, the model's values are set into the view's widgets. Here `v.name` is a `StubTextBox`, and it's `getText()` method is just getting from an internal `text` field instead of any normal `TextBox` DOM coupling.

The next test ensures that the "X left" label updates as the user types:

    @Test
    public void keyUpChangesNameLeft() {
      dto.name = "foo";
      p.bind();
      assertThat(v.name.getText(), is("foo"));
      assertThat(v.nameLeft.getText(), is("47 left"));

      v.name.press('b');
      assertThat(v.nameLeft.getText(), is("46 left"));
    }
{: class=brush:java}

`StubTextBox.press` is a helper method that takes a char and fires dummy key down, key press, and key up events (but no change event or blur event). After calling `press('b')`, we see that the `nameLeft` label got updated to "46 left".

The last test tests the submit button:

    @Test
    public void saving() {
      dto.name = "foo";
      p.bind();
      v.name.type("bar");
      v.submit.click();

      // action went to the server
      assertThat(async.getAction(SaveClientAction.class).getClient().name, is("bar"));

      // save on the server is successful
      async.getCallback(SaveClientAction.class).onSuccess(new SaveClientResult(new ArrayList<String>()));

      assertThat(bus, hasPlaceRequests("clients"));
    }
{: class=brush:java}

And has two asserts:

* That `SaveClientAction` was submitted to the server with a DTO with the new name "bar"
* That after the call succeeded, a new place request for `#clients` was fired on the event bus

