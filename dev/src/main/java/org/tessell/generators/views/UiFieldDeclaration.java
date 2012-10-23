package org.tessell.generators.views;


/** A DTO for {@code ui:with} or {@code ui:field} declarations. */
class UiFieldDeclaration implements Comparable<UiFieldDeclaration> {

  final String type;
  final String name;
  final boolean isElement;

  UiFieldDeclaration(final String type, final String name) {
    this.type = type;
    this.name = name;
    isElement = type.contains("dom");
  }

  @Override
  public int compareTo(final UiFieldDeclaration o) {
    return type.compareTo(o.type);
  }
}
