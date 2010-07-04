package org.gwtmpv.model.properties;

/** Interface for properties that are not necessarily strings but can got into, say, ListBoxes, as strings. */
public interface StringableProperty extends HasRuleTriggers {

  /** @return the property as a string. */
  public String getAsString();

  /**
   * @param value
   *          the String to set, triggers an error message if it is invalid
   */
  void setAsString(String value);

  /**
   * @param fromStringErrorMessage
   *          custom error message for the property being invalid
   */
  void setFromStringErrorMessage(String fromStringErrorMessage);

}
