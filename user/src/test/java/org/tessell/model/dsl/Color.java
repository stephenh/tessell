package org.tessell.model.dsl;

public enum Color {
  GREEN, BLUE;

  @Override
  public String toString() {
    switch (this) {
      case GREEN:
        return "GREEN";
      case BLUE:
        return "Blue (not green)";
    }
    return null;
  }
}