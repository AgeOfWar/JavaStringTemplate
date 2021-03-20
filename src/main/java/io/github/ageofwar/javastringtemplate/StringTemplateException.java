package io.github.ageofwar.javastringtemplate;

public class StringTemplateException extends RuntimeException {
  public StringTemplateException(String message) {
    super(message);
  }
  
  public StringTemplateException(String message, Throwable cause) {
    super(message, cause);
  }
}
