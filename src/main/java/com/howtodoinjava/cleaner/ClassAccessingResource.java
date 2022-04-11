package com.howtodoinjava.cleaner;

import java.lang.ref.Cleaner;

public class ClassAccessingResource implements AutoCloseable {
  
  private final Cleaner cleaner = AppCleanerProvider.getCleaner();
  private final Cleaner.Cleanable cleanable;

  //This resource needs to be cleaned after usage
  private final Resource resource;

  public ClassAccessingResource() {
    this.resource = new Resource();
    this.cleanable = cleaner.register(this, cleanResourceAction(resource));
  }
  
  public void businessOperation() {
    //Access the resource in methods
    System.out.println("Inside businessOperation()");
  }
  
  public void anotherBusinessOperation() {
    //Access the resource in methods
    System.out.println("Inside anotherBusinessOperation()");
  }

  @Override
  public void close() throws Exception {
    cleanable.clean();
  }

  private static Runnable cleanResourceAction(final Resource resource) {
    return () -> {
      // Perform cleanup actions
      // resource.release();
      System.out.println("Resource Cleaned Up !!");
    };
  }
}
