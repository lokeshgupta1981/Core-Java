package com.howtodoinjava.reflection;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class CallingInterfaceMethodWithoutImplementing {

  public static void main(String[] args) {

    //Since Java 16

    Movable movableProxy = (Movable) Proxy.newProxyInstance(
        Movable.class.getClassLoader(),
        new Class<?>[]{Movable.class}, (o, m, p) -> {
          if (m.isDefault()) {
            return InvocationHandler.invokeDefault(o, m, p);
          }
          return null;
        });

    // invoke interface methods
    movableProxy.move();
    String returnedValue = movableProxy.moveWithParamAndReturn("lion");
    System.out.println(returnedValue);

    //Java 15 and Before

    Movable movableProxy2 = (Movable) Proxy.newProxyInstance(
        Movable.class.getClassLoader(),
        new Class<?>[]{Movable.class}, (o, m, p) -> {
          if (m.isDefault()) {
            // Adjust the method type as per the default method's signature
            MethodType methodType;
            if (m.getName().equals("move")) {
              methodType = MethodType.methodType(void.class);
            } else if (m.getName().equals("moveWithParamAndReturn")) {
              methodType = MethodType.methodType(String.class, String.class);
            } else {
              throw new UnsupportedOperationException("Unknown default method: " + m.getName());
            }
            return MethodHandles.lookup()
                .findSpecial(Movable.class, m.getName(),
                    methodType,
                    Movable.class)
                .bindTo(o)
                .invokeWithArguments(p);
          }
          return null;
        });

    movableProxy2.move();
    returnedValue = movableProxy2.moveWithParamAndReturn("lion");
    System.out.println(returnedValue);

    //JDK 8

    Movable movableProxy3 = (Movable) Proxy.newProxyInstance(
        Movable.class.getClassLoader(),
        new Class<?>[]{Movable.class}, (o, m, p) -> {
          if (m.isDefault()) {
            Constructor<Lookup> cntr = Lookup.class
                .getDeclaredConstructor(Class.class);
            cntr.setAccessible(true);
            return cntr.newInstance(Movable.class)
                .in(Movable.class)
                .unreflectSpecial(m, Movable.class)
                .bindTo(o)
                .invokeWithArguments(p);
          }
          return null;
        });

    movableProxy3.move();
    returnedValue = movableProxy3.moveWithParamAndReturn("lion");
    System.out.println(returnedValue);
  }

}

interface Movable {

  default void move() {
    System.out.println("Inside Movable.move()");
  }

  default String moveWithParamAndReturn(String param) {
    System.out.println("Inside Movable.moveWithParamAndReturn()");
    return STR."\{param}-returned";
  }

  void moveToImplement();
}
