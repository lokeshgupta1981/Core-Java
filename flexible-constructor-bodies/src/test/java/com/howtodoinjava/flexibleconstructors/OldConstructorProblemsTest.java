package com.howtodoinjava.flexibleconstructors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OldConstructorProblemsTest {

  @BeforeEach
  void resetCounter() {
    Account.OPENED.set(0);
  }

  @Test
  void givenInvalidRate_whenValidatedAfterSuper_thenAccountWasAlreadyOpened() {
    assertThatThrownBy(() -> new LegacySavingsAccount("ACC-1001", -1.0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Interest rate must be positive: -1.0");

    assertThat(Account.OPENED.get()).isEqualTo(1);
  }

  @Test
  void givenOverriddenMethod_whenCalledFromSuperConstructor_thenSubclassFieldIsNotSetYet() {
    LegacySavingsAccount account = new LegacySavingsAccount("ACC-1001", 3.5);

    assertThat(account.summary()).isEqualTo("Savings ACC-1001 at 0.0%");
    assertThat(account.describe()).isEqualTo("Savings ACC-1001 at 3.5%");
  }

  @Test
  void givenStaticHelper_whenRateIsInvalid_thenSuperConstructorNeverRuns() {
    assertThatThrownBy(() -> new HelperSavingsAccount("ACC-1001", -1.0))
        .isInstanceOf(IllegalArgumentException.class);

    assertThat(Account.OPENED.get()).isZero();
  }
}
