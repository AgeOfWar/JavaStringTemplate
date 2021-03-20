package io.github.ageofwar.javastringtemplate;

public final class Operation {
  private final Resolver resolver;
  private final String path;
  
  public Operation(Resolver resolver, String path) {
    this.resolver = resolver;
    this.path = path;
  }
  
  public Resolver getResolver() {
    return resolver;
  }
  
  public String getPath() {
    return path;
  }
}
