package org.gwtmpv.widgets;

/** An interface for {@link RowTable}. */
public interface HasRows {

  /** Assumes {@code view} is a table and appends any of its TRs to our own table's header. */
  void addHeader(final IsWidget isWidget);

  /** Assumes {@code view} is a table and appends any of its TRs to our own table's body. */
  void addRow(final IsWidget isWidget);

  /** Assumes {@code view} is a table and puts its first TR into row {@code i} of our own table's body. */
  void insertRow(int i, final IsWidget isWidget);

  /** Assumes {@code view} is a table and puts its first TR into row {@code i} of our own table's body. */
  void replaceRow(int i, final IsWidget isWidget);

  /** Assumes row {@code i}. */
  void removeRow(int i);

  /** @return the number of rows shown */
  int size();

}
