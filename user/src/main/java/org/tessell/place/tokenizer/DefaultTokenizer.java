package org.tessell.place.tokenizer;

import org.tessell.place.PlaceRequest;

/**
 * Formats tokens from String values into PlaceRequest values and back again.
 * 
 * This implementation parses the token format like so:
 * 
 * <pre>
 * [name](;param=value)*
 * </pre>
 */
public class DefaultTokenizer implements Tokenizer {

  private final String paramSep;
  private final String valueSep;

  public DefaultTokenizer() {
    this(";", "=");
  }

  public DefaultTokenizer(final String paramSeparator, final String valueSeparator) {
    paramSep = paramSeparator;
    valueSep = valueSeparator;
  }

  public String toHistoryToken(final PlaceRequest request) {
    final StringBuilder out = new StringBuilder();
    out.append(escape(request.getName()));
    for (final String name : request.getParameterNames()) {
      out.append(paramSep);
      out.append(escape(name)).append(valueSep);
      out.append(escape(request.getParameter(name, "")));
    }
    return out.toString();
  }

  public PlaceRequest toPlaceRequest(final String token) throws TokenizerException {
    final int paramSepIndex = token.indexOf(paramSep);
    if (paramSepIndex == -1) {
      return new PlaceRequest(unescape(token));
    } else if (paramSepIndex >= 0) {
      PlaceRequest request = new PlaceRequest(unescape(token.substring(0, paramSepIndex)));
      for (final String paramToken : token.substring(paramSepIndex + 1).split(paramSep)) {
        final String[] param = paramToken.split(valueSep);
        if (param.length == 1) {
          request = request.with(unescape(param[0]), "");
        } else if (param.length == 2) {
          request = request.with(unescape(param[0]), unescape(param[1]));
        } else {
          throw new TokenizerException("Parameters require a single '" + valueSep + "' between the key and value.");
        }
      }
      return request;
    } else {
      throw new TokenizerException("Invalid token " + token);
    }
  }

  protected String escape(final String value) {
    return Codec.encodeQueryString(value);
  }

  protected String unescape(final String value) {
    return Codec.decodeQueryString(value);
  }
}
