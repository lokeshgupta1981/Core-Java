package com.howtodoinjava.java27;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class ProfileLoaderTest {

  @Test
  void joinsBothSubtasks() throws Exception {
    assertThat(ProfileLoader.load(true)).isEqualTo("Lokesh is 37");
  }

  @Test
  void failedSubtaskSurfacesAsExecutionException() {
    assertThatThrownBy(() -> ProfileLoader.load(false))
        .isInstanceOf(ExecutionException.class)
        .hasCauseInstanceOf(IllegalStateException.class)
        .cause().hasMessage("age service down");
  }
}
