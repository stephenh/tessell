package org.tessell.model.properties;

public class Converters {
  
  public static <P> PropertyConverter<P, String> toStringConverter() {
    return new PropertyConverter<P, String>() {
      public String to(P a) {
        return a == null ? null : a.toString();
      }
    };
  }

}
