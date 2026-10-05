package com.howtodoinjava.interview.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TxConfig {

  @Bean
  public LoggingTransactionManager transactionManager() {
    return new LoggingTransactionManager();
  }

  @Bean
  public InvoiceService invoiceService() {
    return new InvoiceService();
  }
}
