package org.tessell.generators.views;

/** A DTO for {@code ui:with} declarations. */
class UiWithDeclaration implements Comparable<UiWithDeclaration> {

  final String type;
  final String name;

  UiWithDeclaration(final String type, final String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public int compareTo(final UiWithDeclaration o) {
    return type.compareTo(o.type);
  }
}
