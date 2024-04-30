package com.howtodoinjava.core.streams;

import java.util.List;
import java.util.stream.Collectors;

public class MapMultiExample {

  public static void main(String[] args) {

    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> multiples = numbers.stream().<Integer>mapMulti((num, downstream) -> {
      downstream.accept(num);
      downstream.accept(num * num); // Add the square of the number
      downstream.accept(num * num * num); // Add the cube of the number
    }).toList();

    System.out.println(multiples); // Output: [2, 3, 4, 6, 6, 9, 8, 12, 10, 15]

    // second example

    // Sample list of transactions
    List<Transaction> transactions = List.of(
        new Transaction("T1", 100, TransactionType.DEPOSIT),
        new Transaction("T2", 50, TransactionType.WITHDRAWAL),
        new Transaction("T3", 200, TransactionType.WITHDRAWAL));

    // Process transactions and generate transaction events
    List<TransactionEvent> transactionEvents = transactions.stream()
        .<TransactionEvent>mapMulti((transaction, downstream) -> {
          if (transaction.getType() == TransactionType.DEPOSIT) {
            downstream.accept(new TransactionEvent("Deposit", transaction.getAmount()));
          } else if (transaction.getType() == TransactionType.WITHDRAWAL) {
            downstream.accept(new TransactionEvent("Withdrawal", transaction.getAmount()));
            // If withdrawal amount is greater than 100, generate an additional fraud alert event
            if (transaction.getAmount() > 100) {
              downstream.accept(new TransactionEvent("Fraud Alert", transaction.getAmount()));
            }
          }
        }).toList();

    // Print the generated transaction events
    transactionEvents.forEach(System.out::println);
  }

  static class Transaction {

    private String id;
    private double amount;
    private TransactionType type;

    public Transaction(String id, double amount, TransactionType type) {
      this.id = id;
      this.amount = amount;
      this.type = type;
    }

    public String getId() {
      return id;
    }

    public double getAmount() {
      return amount;
    }

    public TransactionType getType() {
      return type;
    }
  }

  static class TransactionEvent {

    private String type;
    private double amount;

    public TransactionEvent(String type, double amount) {
      this.type = type;
      this.amount = amount;
    }

    @Override
    public String toString() {
      return "TransactionEvent{" + "type='" + type + '\'' + ", amount=" + amount + '}';
    }
  }

  enum TransactionType {
    DEPOSIT, WITHDRAWAL
  }
}