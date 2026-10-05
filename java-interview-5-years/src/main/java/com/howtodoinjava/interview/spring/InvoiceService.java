package com.howtodoinjava.interview.spring;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class InvoiceService {

  @Transactional
  public boolean save(String item) {
    boolean active = TransactionSynchronizationManager.isActualTransactionActive();
    System.out.println("save(" + item + ") transaction active = " + active);
    return active;
  }

  /** Self-invocation: this.save(...) does not go through the proxy, so no transaction starts. */
  public void saveAll(List<String> items) {
    for (String item : items) {
      save(item);
    }
  }

  /** A checked exception does NOT roll back by default. */
  @Transactional
  public void importFile() throws IOException {
    throw new IOException("file missing");
  }

  @Transactional(rollbackFor = Exception.class)
  public void importFileWithRollback() throws IOException {
    throw new IOException("file missing");
  }
}
