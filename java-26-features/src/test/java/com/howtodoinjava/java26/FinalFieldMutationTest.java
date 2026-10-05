package com.howtodoinjava.java26;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FinalFieldMutationTest {

  static final String MAIN = "com.howtodoinjava.java26.finalfields.FinalFieldMutation";

  @Test
  void defaultModeWarnsButMutates() throws Exception {
    String out = ChildJvm.run(List.of(), MAIN);
    System.out.println(out);
    assertThat(out).contains("WARNING: Final field servings")
        .contains("--enable-final-field-mutation=ALL-UNNAMED")
        .contains("servings = 4");
  }

  @Test
  void denyModeThrowsIllegalAccessException() throws Exception {
    String out = ChildJvm.run(List.of("--illegal-final-field-mutation=deny"), MAIN);
    System.out.println(out);
    assertThat(out).contains("blocked: java.lang.IllegalAccessException");
  }

  @Test
  void enabledMutationIsSilent() throws Exception {
    String out = ChildJvm.run(List.of("--enable-final-field-mutation=ALL-UNNAMED"), MAIN);
    assertThat(out).doesNotContain("WARNING").contains("servings = 4");
  }
}
