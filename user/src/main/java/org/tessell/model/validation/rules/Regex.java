package org.tessell.model.validation.rules;


/** Validates that a property matches a regex. */
public class Regex extends AbstractRule<String> {

  private static final String urlScheme = "(http[s]?):\\/\\/";
  private static final String urlDomain = "(([^:\\/\\s\"]+\\.)+([^:\\/\\s\"]{2,}))"; // foo.com, foo.bar.com
  private static final String urlPath = "(/[^#?\\s\"]*)?";
  private static final String urlQuery = "(\\?[^#\\s\"]*)?";
  private static final String urlHash = "(#[^\\s\"]*)?";
  public static final String URL = "^" + urlScheme + urlDomain + urlPath + urlQuery + urlHash + "$";
  public static final String DOMAIN = "^" + urlDomain + "$";
  public static final String URL_NO_PROTOCOL = "^" + urlDomain + urlPath + urlQuery + urlHash + "$";
  // http://groups.google.com/group/Google-Web-Toolkit/browse_thread/thread/df9ebce869e9c39d
  public static final String EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
  // numeric
  public static final String NUMERIC = "^[0-9]+$";

  private final String regex;

  public Regex(final String message, final String regex) {
    super(message);
    this.regex = regex;
  }

  @Override
  protected boolean isValid() {
    final String value = property.get();
    if (value == null) {
      return true; // defer to a Required rule
    }
    return value.matches(regex);
  }

}
