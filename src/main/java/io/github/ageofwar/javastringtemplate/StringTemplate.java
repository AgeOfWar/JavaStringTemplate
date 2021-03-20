package io.github.ageofwar.javastringtemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public final class StringTemplate {
  
  public static String format(String template, Object args) {
    try {
      return format(new StringReader(template), args);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
  
  public static String format(Reader reader, Object args) throws IOException {
    if (!reader.markSupported()) {
      reader = new BufferedReader(reader);
    }
    StringBuilder builder = new StringBuilder();
    while (true) {
      int c = reader.read();
      if (c == -1) break;
      switch (c) {
        case '\\':
          int escaped = reader.read();
          if (escaped == -1) builder.appendCodePoint(c);
          else builder.appendCodePoint(escaped);
          break;
  
        case '$':
          reader.mark(1);
          int openCurlyBrace = reader.read();
          if (openCurlyBrace == '{') {
            List<Operation> path = readOperations(reader);
            builder.append(Resolver.resolve(args, path));
          } else {
            reader.reset();
            String path = readSimplePath(reader);
            if (path.isEmpty()) builder.append(args);
            else builder.append(PathResolver.INSTANCE.resolve(args, path));
          }
          break;
  
        default:
          builder.appendCodePoint(c);
      }
    }
    return builder.toString();
  }
  
  private static List<Operation> readOperations(Reader reader) throws IOException {
    List<Operation> operations = new ArrayList<>();
    reader.mark(1);
    int operation = reader.read();
    reader.reset();
    if (Character.isJavaIdentifierPart(operation)) {
      String simplePath = readPath(reader);
      operations.add(new Operation(PathResolver.INSTANCE, simplePath));
    }
    while (true) {
      reader.mark(1);
      int separator = reader.read();
      if (separator == '}') break;
      Resolver resolver;
      switch (separator) {
        case '.':
          resolver = PathResolver.INSTANCE;
          break;
          
        case '?':
          resolver = FunctionResolver.INSTANCE;
          break;
          
        case -1:
          throw new StringTemplateException("Unclosed '${'");
          
        default:
          throw new StringTemplateException("Unknown operation '" + separator + "'");
      }
      String path = readPath(reader);
      if (path.isEmpty()) throw new StringTemplateException("Expected path, '" + (char) separator + "' found");
      operations.add(new Operation(resolver, path));
    }
    return operations;
  }
  
  private static String readPath(Reader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    while (true) {
      reader.mark(1);
      int c = reader.read();
      if (c == -1 || c == '}' || c == '?' || c == '.') break;
      builder.appendCodePoint(c);
    }
    reader.reset();
    return builder.toString();
  }
  
  private static String readSimplePath(Reader reader) throws IOException {
    StringBuilder builder = new StringBuilder();
    while (true) {
      reader.mark(1);
      int c = reader.read();
      if (c == -1 || !Character.isJavaIdentifierPart(c) || c == '$') break;
      builder.appendCodePoint(c);
    }
    reader.reset();
    return builder.toString();
  }
}
