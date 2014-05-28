---
layout: default
title: Forms
---

Forms
=====

Tessell has support for building flexible forms with as little boilerplate as possible.

Goals
-----

* Most form fields require 1 line of code--simple is easy
* Custom form fields are still doable--complex is possible
* Consistent look & feel of all forms in an application
* Reusable implementation of standard AJAX form UX
* Low-level customization of the HTML to match a designer's HTML
* Remain unit testable/MVP-compliant

The focus is on getting all of the aspects of an AJAX form's UX right and consistent, without having to repeat all of this logic for every page in your app. Each form field in an application shouldn't require copy/pasting 4-5 lines of code in the `ui.xml` file per form element.

Approach
--------

Tessell's [FormPresenter][FormPresenter] decouples several aspects of forms to ensure a clean separation of concerns. Specifically, decoupling *layout* from *declaration*.

For example, a FormPresenter-enabled form might look like:

    EmployeeModel ee = new EmployeeModel();
    FormPresenter form = new FormPresenter("formId");

    form.add(new TextBoxFormLine(ee.name));
    form.add(new TextBoxFormLine(ee.title));
    form.add(new ListBoxFormLine(ee.status, allPossibleStatuses));
    form.add(new ButtonFormAction(saveCommand, "Save"));

    view.flowPanel().add(form.getView());
{: class="brush:java"}

The page's `ui.xml` file is then as simple as:

    <ui:UiBinder ...>
      <gwt:HTMLPanel>
        <gwt:FlowPanel ui:field="formPanel"/>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class="brush:xml"}

Notice how the presenter can focus on only declaring the form's structure. And notice that no layout details are needed (either here in the presenter or the presenter's `ui.xml` file).

With this setup, the form will automatically get:

* Validation of properties as the user touches each field
* Validation of all properties when the user hits "Save"
* Disabling of "Save"/etc. commands after the initial click

When the form is rendered to the DOM, the `ee.name` property will be wrapped in the boilerplate `div`, `input`, etc. tags to lay out its row. Same thing with the other properties.

By default, the provided `FormLine` implementations will also render errors, so if `ee.name` is required, and the user leaves it blank, the error "Name is required" will be shown.

Form Structure
--------------

FormPresenter is based on the notion of forms compromising of two basic things:

* Lines--typically each form element gets a form line, e.g. the "first name" line or the "last name" line.
* Actions--actions like save/cancel at the end of the form

Then lines themselves are typically composed of:

* Labels--each line has a label, e.g. "First Name".
* Values--each line has a value, e.g. a text box for first name
* Errors--each line has a list of errors, e.g. "Required"

(Note that [FormLine][FormLine] is a generic interface, so is not required to have labels/values/errors. This is convenient for extremely customized form lines, say if you needed a `DashedFormLine` to draw a solid `hr` between two sections of your form.)

With these set of assumptions about form structure, FormPresenter then provides interfaces and some default implementations to generic tie them together in a coherent way.

Layout Customization
--------------------

While FormPresenter provides sensible HTML out-of-the-box, it was designed with the realization that it is often desirable to conform to a project's/mockup's existing HTML/form structure.

To achieve that, FormPresenter defers all HTML logic to the [FormLayout][FormLayout] interface. It has generic "begin/end" methods that allow a layout implementation to drop in the necessary HTML.

For example, [DefaultFormLayout][DefaultFormLayout] look something like:

    public class DefaultFormLayout implements FormLayout {
      @Override
      public void formBegin(FormPresenter p, HTMLPanelBuilder hb) {
        hb.add("<div class=\"" + style.form() + "\">");
      }

      @Override
      public void formEnd(FormPresenter p, HTMLPanelBuilder hb) {
        hb.add("</div>");
      }

      @Override
      public void lineBegin(FormPresenter p, HTMLPanelBuilder hb) {
        hb.add("<li>");
      }

      @Override
      public void lineEnd(FormPresenter p, HTMLPanelBuilder hb) {
        hb.add("</li>");
      }

      // ... more methods ...
{: class="brush:java"}

Where it uses `div`, `ol`, and `li` tags to layout the form elements (based on the markup from [fancy form design](http://articles.sitepoint.com/print/fancy-form-design-css)).

Staying Consistent
------------------

While FormPresenter is easy to use directly, it also works well to create a subclass specifically for your application with standardized/application-specific helper methods that can be reused across your pages.

For example, you might have something like:

    public class AppFormPresenter extends FormPresenter {
      public AppFormPresenter(String id) {
        // AppFormLayout is your app's optional custom layout
        super(id, new AppFormLayout(id));
      }

      public void addTextLine(Property<String> p) {
        // if using tweaked text box form lines
        add(new CustomTextBoxFormLine(p));
      }

      // if your app has a custom `Day` user type
      public void addDateLine(Property<Day> p) {
        // implementation might use DatePicker/whatever
        add(new CustomDayFormLine(p));
      }
    }
{: class="brush:java"}

Note again that this subclass is not required to use FormPresenter, but is a helpful way of centralizing the form layout decisions and making consuming page presenters that much more simple.

Testing
-------

In keeping with Tessell's goal for easy DOM-less unit testing, presenters that use `FormPresenter` can still be unit tested.

Note that because form elements are specified only in the presenter (which is more succinct, and doesn't involve boilerplate elements in the `ui.xml` file), it does mean the presenter's view (which is generated from the `ui.xml` file) won't have methods to access each individual form element for the test to manipulate/assert against (see [tests](tests.html) for the typical way of testing in Tessell).

Instead, we have to grab the form elements by their `id` from FormPresenter's backing HTMLPanel. Although if your form ids match your model property names, you can use some helper methods to do the lookup, e.g.:

    public class FooPresenterTest {

      FooPresenter p;
      StubFooView v;

      @Test
      public void testRequiredFields() {
        FooModel model = setupPresenterFor(new FooDto(...));
        // click without filling anything in
        save().click();

        // ensure no request was sent
        assertThat(async.getOutstanding().size(), is(0));

        // ensure error message was shown
        assertThat(
          errors(model.name).getList(),
          contains("Required"));
      }

      // creates/binds the presenter to a model for the dto
      private FooModel setupPresenterFor(FooDto dto) {
        FooModel model = new FooModel(dto);
        p = bind(new FooPresenter(model));
        v = (StubFooView) p.getView();
      }

      // uses p to find the form element by id
      private StubTextList errors(final Property<?> p) {
        String propertyId = Inflector.camelize(p.getName());
        String fullId = "formId-" + propertyId  + "-errors";
        return (StubTextList) v.findById(fullId);
      }

      // returns the stub button rendered in the form
      private StubButton save() {
        return (StubButton) v.findById("formId-save");
      }
{: class="brush:java"}




[FormPresenter]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/form/FormPresenter.java

[FormLayout]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/form/FormLayout.java

[DefaultFormLayout]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/form/DefaultFormLayout.java

[FormLine]: https://github.com/stephenh/tessell/blob/master/user/src/main/java/org/tessell/widgets/form/lines/FormLine.java
