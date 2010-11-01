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

  public int offset() {
    switch (this) {
      case ASC:
        return 1;
      case DESC:
        return -1;
    }
    assert false : "Should not be sorting on " + this;
    return 1;
  }

}
