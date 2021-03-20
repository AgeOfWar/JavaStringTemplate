# String Template Java Library
## Examples

```java
import io.github.ageofwar.javastringtemplate.StringTemplate;
import java.util.List;

public class Examples {
  public static void main(String[] args) {
    var example1 = StringTemplate.format("Hello $!", "World");
    System.out.println(example1); // Hello World!
  
    var list1 = List.of("World", "David", true);
    var example2 = StringTemplate.format("Hello $0!", list1);
    System.out.println(example2); // Hello World!
  
    var list2 = List.of("World", "David", "Mike");
    var example3 = StringTemplate.format("Hello ${?join(, )}!", list2);
    System.out.println(example3); // Hello World, David, Mike!
  
    var map1 = Map.of("hello", "world", "person", "David");
    var example4 = StringTemplate.format("Hello ${hello?capitalize}!", map1);
    System.out.println(example4); // Hello World!
  
    var point = new Point(0, 0);
    var example5 = StringTemplate.format("origin = $", point);
    System.out.println(example5); // origin = (0,0)
  
    var example6 = StringTemplate.format("zero = $x", point);
    System.out.println(example6); // zero = 0
  
    var list3 = List.of(new Point(0, 0), new Point(3, 5), new Point(8, -2));
    var example7 = StringTemplate.format("first y = ${0.y}, all x = ${?map(x)?join(, )}", list3);
    System.out.println(example7); // first y = 0, all x = 0, 3, 8
  }
}

public class Point {
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
```

## Maven
```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency> 
  <groupId>com.github.AgeOfWar</groupId> 
  <artifactId>JavaStringTemplate</artifactId>
  <version>1.0</version>
</dependency>
```

## Gradle
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.AgeOfWar:JavaStringTemplate:1.0'
}
```