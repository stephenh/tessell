---
layout: default
title: Forms
---

Forms
=====

gwtmpv has support for flushing out forms with as-minimum-as-possible amount of pain.

The focus is currently on reducing boilerplate HTML/UiBinder.

    EmployeeModel model = ...;

    UiCommand command = new UiCommand() {
      // ...
    };

    FormPresenter form = new FormPresenter(widgets);
    form.addTextBox(model.firstName());
    form.addTextBox(model.lastName());
    form.addAction("Submit", command);

    view.flowPanel().add(form.getView());
{: class=brush:java}



