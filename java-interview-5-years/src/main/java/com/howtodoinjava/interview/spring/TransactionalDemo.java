package com.howtodoinjava.interview.spring;

import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TransactionalDemo {

  public static void main(String[] args) {
    try (var context = new AnnotationConfigApplicationContext(TxConfig.class)) {
      InvoiceService invoices = context.getBean(InvoiceService.class);

      System.out.println("--- 1. call through the proxy");
      invoices.save("apple");

      System.out.println("--- 2. self-invocation");
      invoices.saveAll(List.of("banana"));

      System.out.println("--- 3. checked exception, default rules");
      try {
        invoices.importFile();
      } catch (IOException e) {
        System.out.println("caught " + e.getMessage());
      }

      System.out.println("--- 4. checked exception, rollbackFor = Exception.class");
      try {
        invoices.importFileWithRollback();
      } catch (IOException e) {
        System.out.println("caught " + e.getMessage());
      }
    }
  }
}
