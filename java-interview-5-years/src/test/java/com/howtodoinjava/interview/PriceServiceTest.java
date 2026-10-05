package com.howtodoinjava.interview;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.howtodoinjava.interview.testing.PriceService;
import com.howtodoinjava.interview.testing.TaxClient;
import org.junit.jupiter.api.Test;

class PriceServiceTest {

  @Test
  void addsTaxFromClient() {
    TaxClient taxClient = mock(TaxClient.class);
    when(taxClient.taxPercent("IN")).thenReturn(18);
    PriceService service = new PriceService(taxClient);

    int price = service.finalPrice(100, "IN");           // 118
    verify(taxClient).taxPercent("IN");                  // passes, called once with "IN"

    assertThat(price).isEqualTo(118);
  }
}
