---
layout: default
title: Stubs
---

Stubs
=====

Stubs are not a part of traditional MVP, but they are used extensively in `gwt-mpv`.

Each `IsXxx` widget interface is accompanied by the real `GwtXxx` implementation and also a `StubXxx` implementation. For example [IsTextBox](http://github.com/stephenh/gwt-mpv/blob/master/user/src/main/java/org/gwtmpv/widgets/IsTextBox.java), [GwtTextBox](http://github.com/stephenh/gwt-mpv/blob/master/user/src/main/java/org/gwtmpv/widgets/GwtTextBox.java) and [StubTextBox](http://github.com/stephenh/gwt-mpv/blob/master/user/src/main/java/org/gwtmpv/widgets/StubTextBox.java).

While the `GwtXxx` implementation will likely use `GWT.*` and `DOM.*` methods that only work in `GWTTestCase`, the `StubXxx` implementations are pure Java and so can run in blazing fast unit tests.

The strategy of using stubs is in contrast to the supposedly dumb views of [traditional MVP part II](http://code.google.com/webtoolkit/articles/mvp-architecture-2.html). Instead of each MVP view having to recode something like:

    @UiHandler("contactsTable")
    void onTableClicked(ClickEvent event) {
      if (presenter != null) {
        EventTarget target = event.getNativeEvent().getEventTarget();
        Node node = Node.as(target);
        TableCellElement cell = findNearestParentCell(node);
        if (cell == null) {
          return;
        }

        TableRowElement tr = TableRowElement.as(cell.getParentElement());
        int row = tr.getSectionRowIndex();

        if (cell != null) {
          if (shouldFireClickEvent(cell)) {
            presenter.onItemClicked(rowData.get(row));
          }
          if (shouldFireSelectEvent(cell)) {
            presenter.onItemSelected(rowData.get(row));
          }
        }
     }
{: class=brush:java}

Each and every time you want to use a `Table` widget, the `StubTable` encapsulates this logic just once, that then each truly dumb view can reuse.

**Disclaimer: In theory, it has worked for everything else but I haven't actually done a 2.1-style table example yet.**

Stub Per View
-------------

Since `gwt-mpv` is [already generating](viewgeneration.html) the real `XxxView` anyway, it is easy to also generate a `StubXxxView` for testing.

The real `XxxView` will have `@UiField`-annotated fields of the real GWT widgets used at production time, e.g.:

    @UiField(provided = true)
    final TextBox name = new org.gwtmpv.widgets.GwtTextBox();
{: class=brush:java}

While the `StubXxxView` will have `StubXxx` widgets to use at test time, e.g.:

    final StubTextBox name = new org.gwtmpv.widgets.StubTextBox();
{: class=brush:java}

So, just as the `IsXxx` widget interfaces allowed the real `ClientView` to be so truly dumb that it could be generated, the `StubXxx` widget implementations allows the test `StubClientView` to be so truly dumb that it can be generated as well.

`AppViews` Interface
--------------------

To manage which view should be used (`XxxView` vs. `StubXxxView`) when your presenter needs it, `gwt-mpv` also generates an `AppView` interface with methods to create each of your views.

For example, `gwt-hack` looks something like:

    public interface AppViews {
        IsAppView getAppView();

        IsClientView getClientView();

        IsClientListView getClientListView();

        IsEmployeeView getEmployeeView();

        IsEmployeeListView getEmployeeListView();
    }
{: class=brush:java}

Two implementations of `AppViews` are generated as well:

* `GwtViews`, whose methods return the corresponding real UiBinder-powered `XxxView`, and
* `StubViews`, whose methods return the corresponding doubled `StubXxxView`.

This means instead of having to repeat the conditional "bind the real `XxxView` or the stub `StubXxxView`" for every single view in your app, you can make just one decision of `GwtViews` vs. `StubViews` in your gin module/`AppRegistry`.

Stubs Are Great
---------------

For more on stubs, see:

* [Mocks aren't Stubs](http://martinfowler.com/articles/mocksArentStubs.html), and
* [Why I Don't Like Mocks](http://www.draconianoverlord.com/2010/07/09/why-i-dont-like-mocks.html)


