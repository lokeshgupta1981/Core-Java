package com.howtodoinjava.flexibleconstructors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FlexibleConstructorTest {

  @BeforeEach
  void resetCounter() {
    Account.OPENED.set(0);
  }

  @Test
  void givenInvalidRate_whenValidatedBeforeSuper_thenAccountIsNeverOpened() {
    assertThatThrownBy(() -> new SavingsAccount("ACC-1001", -1.0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Interest rate must be positive: -1.0");

    assertThat(Account.OPENED.get()).isZero();
  }

  @Test
  void givenFieldAssignedBeforeSuper_whenSuperCallsOverriddenMethod_thenFieldIsVisible() {
    SavingsAccount account = new SavingsAccount("ACC-1001", 3.5);

    assertThat(account.summary()).isEqualTo("Savings ACC-1001 at 3.5%");
  }

  @Test
  void givenRecordString_whenParsedBeforeThis_thenFieldsAreSet() {
    SavingsAccount account = new SavingsAccount("ACC-2002; 4.25");

    assertThat(account.id()).isEqualTo("ACC-2002");
    assertThat(account.interestRate()).isEqualTo(4.25);
  }

  @Test
  void givenText_whenRecordConstructorParsesBeforeThis_thenMoneyIsCreated() {
    Money price = new Money("19.99 USD");

    assertThat(price.amount()).isEqualByComparingTo(new BigDecimal("19.99"));
    assertThat(price.currency()).isEqualTo("USD");
  }

  @Test
  void givenBadText_whenRecordConstructorValidates_thenFailsFast() {
    assertThatThrownBy(() -> new Money("19.99"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Expected '<amount> <currency>': 19.99");
  }

  @Test
  void givenThreeLevels_whenConstructed_thenProloguesRunUpAndEpiloguesRunDown() {
    List<String> log = new ArrayList<>();

    new ConstructionOrder.Leaf(log);

    assertThat(log).containsExactly(
        "Leaf prologue", "Middle prologue", "Base prologue",
        "Base epilogue", "Middle epilogue", "Leaf epilogue");
  }
}
