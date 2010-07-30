package org.gwtmpv.widgets.cellview;

/** Whether a column is not/is asc/is desc. */
public enum Sorted {

  NO("&nbsp;"), ASC("\u25BE"), DESC("\u25B4");

  private String icon;

  private Sorted(final String icon) {
    this.icon = icon;
  }

  public String icon() {
    return icon;
  }

  public Sorted toggle() {
    switch (this) {
    case NO:
      return ASC;
    case ASC:
      return DESC;
    case DESC:
      return ASC;
    default:
      return ASC;
    }
  }

}