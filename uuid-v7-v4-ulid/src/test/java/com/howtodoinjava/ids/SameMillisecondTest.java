package com.howtodoinjava.ids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SameMillisecondTest {

  private static <T> List<T> burst(Supplier<T> generator) {
    List<T> ids = new ArrayList<>();
    for (int i = 0; i < 1_000; i++) {
      ids.add(generator.get());
    }
    return ids;
  }

  @Test
  void givenRandomV7InOneMillisecond_whenSorting_thenGenerationOrderIsLost() {
    long now = System.currentTimeMillis();
    List<UUID> ids = burst(() -> UuidV7.of(now));

    assertThat(ids.stream().sorted().toList()).isNotEqualTo(ids);
  }

  @Test
  void givenPlainUlid_whenBurstOfIds_thenGenerationOrderIsLost() {
    List<Ulid> ids = burst(UlidCreator::getUlid);

    assertThat(ids.stream().sorted().toList()).isNotEqualTo(ids);
  }

  @Test
  void givenMonotonicGenerators_whenBurstOfIds_thenGenerationOrderIsKept() {
    List<UUID> v7 = burst(UuidCreator::getTimeOrderedEpoch);
    List<Ulid> ulids = burst(UlidCreator::getMonotonicUlid);

    assertThat(v7).isSorted();
    assertThat(ulids).isSorted();
  }
}
