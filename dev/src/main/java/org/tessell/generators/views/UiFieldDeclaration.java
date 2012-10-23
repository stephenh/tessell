package org.tessell.generators.views;

/** A DTO for {@code ui:field} declarations. */
class UiFieldDeclaration implements Comparable<UiFieldDeclaration> {

  final String type;
  final String name;
  final String parentName;
  final boolean isElement;

  UiFieldDeclaration(final String type, final String name, final String parentName) {
    this.type = type;
    this.name = name;
    this.parentName = parentName;
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
