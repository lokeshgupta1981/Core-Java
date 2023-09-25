package com.howtodoinjava.newFeatures.java21;

//import java.util.concurrent.StructuredTaskScope;

public class ScopedValueTest {

  //public static ThreadLocal<String> CONTEXT = ThreadLocal.withInitial(() -> null);
  //public static InheritableThreadLocal<String> CONTEXT = new InheritableThreadLocal();

  //private static final ScopedValue<String> CONTEXT = ScopedValue.newInstance();



  public static void main(String[] args) {



    /*ScopedValue.runWhere(CONTEXT, "TestValue", () -> {

      doSomething();

      try (var scope = new StructuredTaskScope<String>()) {

        scope.fork(() -> insideChildThread());

        try{
          scope.join();
        } catch (InterruptedException ex) {
          //...
        }
        
      }
    });*/


    ScopedValueTest instance = new ScopedValueTest();

    /*ScopedValue.runWhere(CONTEXT, "Test Value", () -> {

      System.out.println("In parent thread start the scoped value is: " + CONTEXT.get());
      instance.doSomething();
      System.out.println("In parent thread end the scoped value is: " + CONTEXT.get());
    });

    System.out.println("Outside bounded scope isBound() is: " + CONTEXT.isBound()); 
    System.out.println("Outside bounded scope the scoped value is: " + CONTEXT.orElse(null));*/
  

  











    /*Thread parentThread = new Thread(() -> {

      CONTEXT.set("TestValue");
      insideParentThread_1();

      Thread childThread = new Thread(() -> {
          insideChildThread();
      });

      childThread.start();
      insideParentThread_2();
    });

    parentThread.start();*/
  }

  /*public void doSomething() {
    System.out.println("In doSomething() and parent scope: " + CONTEXT.get());
    ScopedValue.runWhere(CONTEXT, "Changed Value", () -> {
      System.out.println("In doSomething() and child scope: " + CONTEXT.get());
      doSomethingAgain();
    });
  }*/

  /*public void doSomethingAgain() {
    System.out.println("In doSomethingAgain() and child scope: " + CONTEXT.get());
  }*/

  /*static void doSomething() {
      System.out.println("Scoped Value in doSomething(): " + CONTEXT.get());
  }

  static void insideParentThread_1() {
      System.out.println("ThreadLocal Value in insideParentThread_1(): " + CONTEXT.get());
  }

  static void insideParentThread_2() {
      System.out.println("ThreadLocal Value in insideParentThread_2(): " + CONTEXT.get());
  }

  static String insideChildThread() {
      System.out.println("Scoped Value in insideChildThread(): " + CONTEXT.get());
      return CONTEXT.get();
  }*/
}
