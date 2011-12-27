package org.tessell.place.tokenizer;

/** Thrown when a history token cannot be parsed. */
public class TokenizerException extends Exception {

  private static final long serialVersionUID = 1L;

  public TokenizerException(final String message) {
    super(message);
  }

}
