package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.howtodoinjava.interview.spring.InvoiceService;
import com.howtodoinjava.interview.spring.LoggingTransactionManager;
import com.howtodoinjava.interview.spring.TxConfig;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class SpringTransactionalTest {

  @Test
  void proxyAndRollbackRules() {
    try (var context = new AnnotationConfigApplicationContext(TxConfig.class)) {
      InvoiceService invoices = context.getBean(InvoiceService.class);
      LoggingTransactionManager tx = context.getBean(LoggingTransactionManager.class);

      assertThat(AopUtils.isCglibProxy(invoices)).isTrue();
      assertThat(invoices.save("apple")).isTrue();
      assertThat(tx.events()).containsExactly(
          "BEGIN com.howtodoinjava.interview.spring.InvoiceService.save", "COMMIT");

      tx.events().clear();
      invoices.saveAll(List.of("banana"));
      assertThat(tx.events()).isEmpty();                 // self-invocation: no transaction

      tx.events().clear();
      assertThatThrownBy(invoices::importFile).isInstanceOf(IOException.class);
      assertThat(tx.events()).endsWith("COMMIT");        // checked exception commits

      tx.events().clear();
      assertThatThrownBy(invoices::importFileWithRollback).isInstanceOf(IOException.class);
      assertThat(tx.events()).endsWith("ROLLBACK");
    }
  }
}
