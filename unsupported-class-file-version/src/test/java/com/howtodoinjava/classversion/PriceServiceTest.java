package com.howtodoinjava.classversion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.howtodoinjava.classversion.PriceService.PriceRepository;
import org.junit.jupiter.api.Test;

/**
 * Mockito creates mocks with Byte Buddy. A Byte Buddy version that does not know
 * the running Java version fails here before the test body runs.
 */
class PriceServiceTest {

  @Test
  void mockitoMocksAClassOnTheCurrentJdk() {
    PriceRepository repository = mock(PriceRepository.class);
    when(repository.priceOf("apple")).thenReturn(50);

    PriceService service = new PriceService(repository);

    assertThat(service.priceWithTax("apple")).isEqualTo(55);
  }
}
