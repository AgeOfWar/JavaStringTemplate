package io.github.ageofwar.javastringtemplate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.util.Locale.ENGLISH;

public class PathResolver implements Resolver {
  public static final PathResolver INSTANCE = new PathResolver();
  
  private PathResolver() {
  }
  
  @Override
  public Object resolve(Object object, String path) {
    Class<?> clazz = object.getClass();
    try {
      return invokePrivate(clazz.getMethod("get" + capitalize(path)), object);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e1) {
      try {
        return invokePrivate(clazz.getMethod("is" + capitalize(path)), object);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e2) {
        try {
          return invokePrivate(clazz.getMethod("get", String.class), object, path);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e3) {
          try {
            return invokePrivate(clazz.getMethod("get", int.class), object, Integer.parseInt(path));
          } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NumberFormatException e4) {
            try {
              return invokePrivate(clazz.getMethod("get", Object.class), object, path);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NumberFormatException e5) {
              throw new StringTemplateException("Cannot resolve path '" + path + "' of " + clazz, e5);
            }
          }
        }
      }
    }
  }
  
  private static String capitalize(String name) {
    if (name == null || name.length() == 0) {
      return name;
    }
    return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
  }
  
  private static Object invokePrivate(Method method, Object obj, Object... args) throws InvocationTargetException, IllegalAccessException {
    method.setAccessible(true);
    return method.invoke(obj, args);
  }
}
