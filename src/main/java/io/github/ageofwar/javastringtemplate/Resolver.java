package io.github.ageofwar.javastringtemplate;

import java.util.List;

public interface Resolver {
  Object resolve(Object object, String path);
  
  static Object resolve(Object object, List<Operation> operations) {
    for (Operation operation : operations) {
      object = operation.getResolver().resolve(object, operation.getPath());
    }
    return object;
  }
}
