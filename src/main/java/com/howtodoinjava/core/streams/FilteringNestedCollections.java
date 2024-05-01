package com.howtodoinjava.core.streams;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

public class FilteringNestedCollections {

  public static void main(String[] args) {

    List<AccountStatement> accStmts = List.of(
        new AccountStatement(1, "A001", List.of(
            new Transaction(1, 100, "A001"),
            new Transaction(2, 60, "A001"),
            new Transaction(3, 550, "A001"))),
        new AccountStatement(2, "A002", List.of(
            new Transaction(4, 200, "A002"),
            new Transaction(5, 160, "A002"),
            new Transaction(6, 100, "A002"))),
        new AccountStatement(3, "A003", List.of(
            new Transaction(7, 10, "A003"),
            new Transaction(8, 20, "A003"),
            new Transaction(9, 3040, "A003"))));

    List<Transaction> transactionsMoreThan500 = accStmts.stream()
        .flatMap(stmt -> stmt.getTransactions().stream())
        .filter(transaction -> transaction.getAmount() > 500)
        .collect(Collectors.toList());

    System.out.println(transactionsMoreThan500);

    List<Transaction> transactionsMoreThan500_V2 = accStmts.stream()
        .<Transaction>mapMulti((stmt, consumer) -> {
          for (Transaction t : stmt.getTransactions()) {
            if (t.getAmount() > 500) {
              consumer.accept(t);
            }
          }
        })
        .collect(Collectors.toList());

    System.out.println(transactionsMoreThan500_V2);

    List<AccountStatement> stmtHavingTransactionsMoreThan500 = accStmts.stream()
        .<AccountStatement>mapMulti((stmt, consumer) -> {
          for (Transaction t : stmt.getTransactions()) {
            if (t.getAmount() > 500) {
              consumer.accept(stmt);
              break;
            }
          }
        })
        .collect(Collectors.toList());

    System.out.println(stmtHavingTransactionsMoreThan500);

    List<AccountStatement> stmtHavingTransactionsMoreThan500_V2 = accStmts.stream()
        .filter(
            stmt -> stmt.getTransactions()
                .stream()
                .anyMatch(transaction -> transaction.getAmount() > 500)
        )
        .collect(Collectors.toList());

    System.out.println(stmtHavingTransactionsMoreThan500_V2);
  }
}

@Data
@AllArgsConstructor
class AccountStatement {

  private long id;
  private String accountNumber;
  private List<Transaction> transactions;
}

@Data
@AllArgsConstructor
class Transaction {

  private long id;
  private double amount;
  private String accountNumber;
}
