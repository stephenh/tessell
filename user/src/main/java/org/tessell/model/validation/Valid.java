package org.tessell.model.validation;

public enum Valid {
  TRUE, FALSE, PENDING;

  public static Valid fromBoolean(Boolean b) {
    if (b == null) {
      return PENDING;
    } else {
      if (b) {
        return TRUE;
      } else {
        return FALSE;
      }
    }
  }
}
