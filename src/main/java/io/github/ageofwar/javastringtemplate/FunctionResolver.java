package io.github.ageofwar.javastringtemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class FunctionResolver implements Resolver {
  public static final FunctionResolver INSTANCE = new FunctionResolver();
  
  private FunctionResolver() {
  }
  
  @Override
  public Object resolve(Object object, String path) {
    String[] parts = path.split("\\(");
    String name = parts[0];
    String p1;
    try {
      switch (parts.length) {
        case 1:
          switch (name) {
            case "upper":
              return upper(object);
            case "lower":
              return lower(object);
            case "capitalize":
              return capitalize(object);
            default:
              throw new StringTemplateException("Unknown function '" + name + "'");
          }
          
        case 2:
          if (!parts[1].endsWith(")")) {
            throw new StringTemplateException("Unclosed '(' in function '" + name + "'");
          }
          p1 = parts[1].substring(0, parts[1].length() - 1);
          switch (name) {
            case "date":
              return date(object, p1);
            case "time":
              return time(object, p1);
            case "join":
              return join(object, p1);
            case "map":
              return map(object, p1);
            default:
              throw new StringTemplateException("Unknown function '" + name + "'");
          }
          
        default:
          throw new StringTemplateException("Too many arguments for function '" + name + "'");
      }
    } catch (Exception e) {
      throw new StringTemplateException("Error in function '" + name + "'", e);
    }
  }
  
  private String date(Object object, String pattern) {
    if (object instanceof Date) {
      return new SimpleDateFormat(pattern).format((Date) object);
    } else if (object instanceof Long) {
      return new SimpleDateFormat(pattern).format(new Date((long) object));
    } else if (object instanceof Integer) {
      return new SimpleDateFormat(pattern).format(new Date((long) object * 1000L));
    } else if (object instanceof LocalDateTime) {
      return ((LocalDateTime) object).format(DateTimeFormatter.ofPattern(pattern));
    } else if (object instanceof LocalDate) {
      return ((LocalDate) object).format(DateTimeFormatter.ofPattern(pattern));
    } else if (object instanceof LocalTime) {
      return ((LocalTime) object).format(DateTimeFormatter.ofPattern(pattern));
    }
    throw new ClassCastException();
  }
  
  private String time(Object object, String pattern) {
    if (object instanceof Date) {
      return new SimpleDateFormat(pattern).format((Date) object);
    } else if (object instanceof Long) {
      return new SimpleDateFormat(pattern).format(new Date((long) object));
    } else if (object instanceof Integer) {
      return new SimpleDateFormat(pattern).format(new Date((long) object * 1000L));
    } else if (object instanceof LocalDateTime) {
      return ((LocalDateTime) object).format(DateTimeFormatter.ofPattern(pattern));
    } else if (object instanceof LocalDate) {
      return ((LocalDate) object).format(DateTimeFormatter.ofPattern(pattern));
    } else if (object instanceof LocalTime) {
      return ((LocalTime) object).format(DateTimeFormatter.ofPattern(pattern));
    }
    throw new ClassCastException();
  }
  
  private String format(Object object, String pattern) {
    try {
      return new DecimalFormat(pattern).format(object);
    } catch (IllegalArgumentException e) {
      throw new ClassCastException();
    }
  }
  
  private String join(Object object, String delimiter) {
    StringJoiner joiner = new StringJoiner(delimiter);
    if (object instanceof Iterable<?>) {
      for (Object element : (Iterable<?>) object) {
        joiner.add(element.toString());
      }
    } else if (object instanceof Object[]) {
      for (Object element : (Object[]) object) {
        joiner.add(element.toString());
      }
    } else {
      throw new ClassCastException();
    }
    return joiner.toString();
  }
  
  private List<Object> map(Object object, String path) {
    List<Object> list = new ArrayList<>();
    if (object instanceof Object[]) {
      for (Object element : (Object[]) object) {
        list.add(PathResolver.INSTANCE.resolve(element, path));
      }
    } if (object instanceof Iterable<?>) {
      for (Object element : (Iterable<?>) object) {
        list.add(PathResolver.INSTANCE.resolve(element, path));
      }
    } else {
      throw new ClassCastException();
    }
    return list;
  }
  
  private static String capitalize(Object object) {
    String s = object.toString();
    if (s == null || s.length() == 0) {
      return s;
    }
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }
  
  private static String upper(Object object) {
    return object.toString().toUpperCase();
  }
  
  private static String lower(Object object) {
    return object.toString().toLowerCase();
  }
}
