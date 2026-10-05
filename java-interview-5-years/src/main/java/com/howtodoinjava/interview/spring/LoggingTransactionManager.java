package com.howtodoinjava.interview.spring;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

/** A transaction manager without a database that records BEGIN, COMMIT and ROLLBACK. */
public class LoggingTransactionManager extends AbstractPlatformTransactionManager {

  private final List<String> events = new ArrayList<>();

  public List<String> events() {
    return events;
  }

  private void log(String event) {
    events.add(event);
    System.out.println(event);
  }

  @Override
  protected Object doGetTransaction() {
    return new Object();
  }

  @Override
  protected void doBegin(Object transaction, TransactionDefinition definition) {
    log("BEGIN " + definition.getName());
  }

  @Override
  protected void doCommit(DefaultTransactionStatus status) {
    log("COMMIT");
  }

  @Override
  protected void doRollback(DefaultTransactionStatus status) {
    log("ROLLBACK");
  }
}
