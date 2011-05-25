---
layout: default
title: Rich Models
---

Rich Models
===========

gwt-mpv encourages rich, event-driven models.

Rather than trying to keep view/model in sync via imperative programming (lots of inner classes), rich models enable declarative programming that can lead to more reliable UI behavior and succinct code.

Examples
--------

For example, a model might look like:

    class EmployeeModel {
      public final IntegerProperty id = intProperty("id");
      public final StringProperty name = stringProperty("name");
    }
{: class=brush:java}

The employee's attributes being `Property` objects instead of regular primitives/value objects means they can be used declaratively, e.g.:

    class EmployeePresenter {
      public void onBind() {
        super.onBind();
        binder.bind(employee.id).to(view.id());
        binder.bind(employee.name).to(view.name());
      }
    }
{: class=brush:java}

This sets up two-way data binding. If your business-logic calls:

    employee.name.set("foo");
{: class=brush:java}

The view's text box will be updated. If the user changes the view, the model employee's name will be updated.

Models and properties have the notion of validation as well, so you can mark `id` as required, and then bind any error messages to the UI, say as a bulleted list:

    class EmployeeModel {
      public final IntegerProperty id = intProperty("id").req();
    }

    class EmployeePresenter {
      public void onBind() {
        super.onBind();
        binder.bind(employee.id).to(view.id(), view.idErrors());
      }
    }
{: class=brush:java}

As the user fills in/clears the Id text field in the view, the property will re-validate itself and fire validation events, and the `idErrors` list will show/hide the messages to the user.

The key take away here is how the 1-line declarations are setting up implicit behavior instead of the presenter tediously coding out inner classes that say "on text change, put the id here, then run the validation, then add/show some stuff".

General Pattern
---------------

Binding declarations typically are of the form:

    bind <property> to <source>
    when <property> <condition> do <something>
{: class=brush:plain}

E.g.:

    binder.bind(model.name).to(view.nameBox());
    binder.bind(model.companyName).to(view.listBox(), listOfCompanyNames);
    binder.when(model.name).is("Bob").show(view.bobsSpecialNote());
    binder.when(model.name).is("Fred").set(s.bold()).on(view.nameBox());
    binder.bind(saveCommand).to(view.submitButton());
{: class=brush:java}

Each declaration means:

* Commonly-used behavior (`show`, `hide`, etc.) can be written once and reused many times instead of being repeated in each presenter as inner classes
* Behavior is automatically re-evaluated whenever properties change or events occur

This should enable your model and view to stay in sync without the, in my experience, usual spaghetti code involved in rich UI applications.

Hamcrest Approach
-----------------

The `binder.xxx` methods are eventually going to be phased out in favor of Hamcrest-style static methods. This will allow much easier user extensibility for providing custom conditions and actions. For example:

    import static org.gwtmpv.model.dsl2.Bind.*;

    bind(model.name, to(view.nameBox()));
    when(model.name, is("Bob"), show(view.bobsSpecialNote()));
{: class=brush:java}

