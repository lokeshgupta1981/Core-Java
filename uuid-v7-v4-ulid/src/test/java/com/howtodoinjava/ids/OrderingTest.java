package com.howtodoinjava.ids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderingTest {

  private static <T> List<T> createSlowly(Supplier<T> generator) throws InterruptedException {
    List<T> ids = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      ids.add(generator.get());
      Thread.sleep(2);
    }
    return ids;
  }

  @Test
  void givenIdsCreatedOverTime_whenSortingV7_thenCreationOrderIsKept() throws InterruptedException {
    List<String> created = createSlowly(() -> UuidCreator.getTimeOrderedEpoch().toString());

    assertThat(created).isSorted();
  }

  @Test
  void givenIdsCreatedOverTime_whenSortingUlid_thenCreationOrderIsKept() throws InterruptedException {
    List<String> created = createSlowly(() -> UlidCreator.getUlid().toString());

    assertThat(created).isSorted();
  }

  @Test
  void givenIdsCreatedOverTime_whenSortingV4_thenCreationOrderIsLost() throws InterruptedException {
    List<String> created = createSlowly(() -> UUID.randomUUID().toString());

    assertThat(created.stream().sorted().toList()).isNotEqualTo(created);
  }

  @Test
  void givenV7Uuids_whenUsingCompareTo_thenOrderMatchesCreationOrder() throws InterruptedException {
    List<UUID> created = createSlowly(UuidCreator::getTimeOrderedEpoch);

    assertThat(created).isSorted();
  }
}
