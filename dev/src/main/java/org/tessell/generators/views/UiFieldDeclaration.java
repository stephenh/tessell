package org.tessell.generators.views;

/** A DTO for {@code ui:field} declarations. */
class UiFieldDeclaration implements Comparable<UiFieldDeclaration> {

  final String type;
  final String name;
  final String parentOperation;
  final boolean isElement;

  UiFieldDeclaration(final String type, final String name, final String parentOperation) {
    this.type = type;
    this.name = name;
    this.parentOperation = parentOperation;
    isElement = type.contains("dom");
  }

  @Override
  public int compareTo(final UiFieldDeclaration o) {
    return type.compareTo(o.type);
  }

  public boolean isAnonymous() {
    return name.startsWith("_anonymous");
  }

}
