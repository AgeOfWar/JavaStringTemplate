package io.github.ageofwar.javastringtemplate.test;

import io.github.ageofwar.javastringtemplate.StringTemplate;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringTemplateTest {
  
  private static final Object ARGS = List.of(new Point(1, 2), new Point(2, 4), List.of("hello", "world"));
  
  @Test
  public void testEmpty() {
    var s = StringTemplate.format("", ARGS);
    assertTrue(s.isEmpty());
  }
  
  @Test
  public void testUnusedArgs() {
    var s = StringTemplate.format("hello world!", ARGS);
    assertEquals("hello world!", s);
  }
  
  @Test
  public void testComplexArgs() {
    var s = StringTemplate.format("all = $\nfirst = $0\nfirst x = ${0.x}\nupper = ${?upper}", ARGS);
    assertEquals("all = " + ARGS + "\nfirst = " + new Point(1, 2) + "\nfirst x = 1\nupper = " + ARGS.toString().toUpperCase(), s);
  }
  
  @Test
  public void testMap() {
    var s = StringTemplate.format("x = $x, y = $y", Map.of("x", "Hello", "y", 5, "z", true));
    assertEquals("x = Hello, y = 5", s);
  }
}

class Point {
  private final int x;
  private final int y;
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }
  @Override
  public String toString() {
    return "(" + x + "," + y + ")";
  }
}
