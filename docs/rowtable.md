---
layout: default
title: Row Table
---

Row Table
=========

The RowTable widget is one of two primary ways to do tables in Tessell. The other is [CellTables](celltable.html).

Benefits
--------

RowTable is most appropriate when:

* You have 10s of rows
* Rows have non-trivial HTML layout that is best done in UiBinder
* Rows have non-trivial logic that deserves it's own presenter
* You want to reuse existing widgets

Drawbacks
---------

* Uses conventional widgets so won't scale to 100s/1000s of rows
* Involves multiple `ui.xml` files that can be unnecessary overhead for very simple layouts

For really simple tables, or really large tables, [CellTable](celltable.html) is a better choice.

Approach
--------

To use RowTable, you create two presenters and three `ui.xml` files:

* `PagePresenter` is the presenter for the page
* `RowPresenter` is the presenter for each table row
* `Page.ui.xml` is the HTML for the page that contains the table
* `Header.ui.xml` is the HTML for the table header row
* `Row.ui.xml` is the HTML for each of the table body rows

While there is just one `PagePresenter` instance for the top-level page, there will be multiple `RowPresenter` instances, one for each table row.

This presenter-per-row allows you to easily hook into the row's widgets and perform per-row business logic (change row styles, make AJAX calls, etc.).

Simple Example
--------------

For example, given a page with an employees table, we'd have `Page.ui.xml`:

    <ui:UiBinder ...>
      <gwt:HTMLPanel>
        ... various html ...

        <mpv:RowTable ui:field="employeeTable"/>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class="brush:xml"}

`EmployeeHeader.ui.xml`:

    <ui:UiBinder ...>
      <gwt:HTMLPanel tag="table">
        <tr>
          <th>Name</th>
          <th>Description</th>
          <th>Actions</th>
        </tr>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class="brush:xml"}

`EmployeeRow.ui.xml`:

    <ui:UiBinder ...>
      <gwt:HTMLPanel tag="table">
        <tr>
          <td><gwt:InlineLabel ui:field="name"/></td>
          <td><gwt:InlineLabel ui:field="description"/></td>
          <td>
            <gwt:Anchor ui:field="edit" text="Edit"/>
            <gwt:Anchor ui:field="delete" text="Delete"/>
          </td>
        </tr>
      </gwt:HTMLPanel>
    </ui:UiBinder>
{: class="brush:xml"}

Tessell's [view generation](viewgeneration.html) will turn these into 3 views, `IsPageView`, `IsEmployeeHeaderView`, and `IsEmployeeRowView`.

The `PagePresenter` fetches the table data, instantiates an inner-class presenter around each row's data object, and adds the row's view to the table, e.g.:

    public class PagePresenter extends AbstractPresenter<IsPageView> {

      // ... fetch data from server logic ...

      // called when we get data from the server
      private void onData(ArrayList<Employee> employees) {
        // header doesn't have any logic, so add
        // it's view directly to the table
        view.employeeTable().addHeader(newEmployeeHeaderView());

        // now add each row's view, with each row
        // getting it's own presenter
        for (Employee employee : employees) {
          EmployeeRowPresenter p = addPresenter(new EmployeeRowPresenter(employee));
          view.employeeTable().addRow(p.getView());
        }
      }

      private class EmployeeRowPresenter extends AbstractPresenter<IsEmployeeRowView> {
        private final Employee employee;

        private EmployeeRowPresenter(Employee employee) {
          super(newEmployeeRowView());
          this.employee = employee;
        }

        @Override
        public void onBind() {
          super.onBind();
          view.name().setText(employee.name);
          view.description().setText(employee.description);
          registerHandler(view.edit().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
              // do logic
            }
          }));
        }
      }
    }
{: class="brush:java"}

While this example doesn't do anything fancy, the idea is that the per-row, stateful `EmployeeRowPresenter` will scale as your table logic becomes more complex.

Testing
-------

Testing RowTable works much like regular Tessell stub-based testing.

    public class PagePresenterTest {
      final PagePresenter p = bind(new PagePresenter(registry));
      final StubPageView v = (StubPageView) p.getView();

      @Test
      public void nameIsSet() {
        doDataResult();
        assertThat(row(0).name.getText(), is("Employee 1"));
        assertThat(row(1).name.getText(), is("Employee 2"));
      }

      @Test
      public void deleteDoesSomething() {
        doDataResult();
        row(0).edit.click();
        // assert whatever happened
      }

      // returns row {@code i} from the page's row table
      private StubEmployeeRowView row(int i) {
        StubRowTable table = (StubRowTable) v.rowTable();
        return (StubEmployeeRowView) table.getRows().get(i);
      }

      private void doDataResult() {
        // do async callback/something that results in
        // PagePresenter.onData being called
      }
    }
{: class="brush:java"}

Implementation Details
----------------------

Internally, RowTable adopts each row's HTMLPanel as a child widget, so GWT events will work. It then steals the HTMLPanel's first `tr` element and appends it to the RowTable's own `tbody` element.

Granted, having an `<gwt:HTMLPanel tag="table">` for each row looks odd (it insinuates that each row is its own table). However, due to the way browsers' `innerHTML` works (which HTMLPanel uses), a root `tag="tr"` does not work. Without the root `table` tag, the lone `tr` is invalid markup and all of the HTML gets dropped.


