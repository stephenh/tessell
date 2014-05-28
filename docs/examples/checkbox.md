---
layout: default
title: Checkbox Example
---

{{page.title}}
==============

A simple example of binding a property to a checkbox and doing some logic based on the selected value.

We'll start with a really simple template file, that just has a CheckBox and a Label:

{% example src/main/java/org/tessell/examples/client/views/CheckboxExample.ui.xml %}

Which, from this, Tessell will generate the view interface:

{% example ./target/generated/java/org/tessell/examples/client/views/IsCheckboxExampleView.java %}

For our example, we just want to:

* When the checkbox is checked, update the label with "Checked!" or "Not checked"

With Tessell's stub widgets, we could actually implement this the old fashioned way, with event handlers:

    view.box().addValueChangedHandler(new ValueChangedHandler<Boolean>()) {
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        view.label().setText(event.getValue() ? "Checked!" : "Not checked");
      }
    }
{: class="brush:java"}

But, for the purposes of this example, we'll use Tessell's reactive features to bind the checkbox to a property, create a derived property, and bind that derived property to the label.

{% example src/main/java/org/tessell/examples/client/app/CheckboxExamplePresenter.java %}

Since the `DerivedProperty` we create uses `checked.isTrue()`, any time `checked` changes, our `getDerivedValue()` method will be called, and the derived property will update itself.

And since we've bound the `DerivedProperty` to the `view.label()`, then the label's text will also get updated.

We can unit test this functionality in a presenter test:

{% example src/test/java/org/tessell/examples/client/app/CheckboxExamplePresenterTest.java %}



