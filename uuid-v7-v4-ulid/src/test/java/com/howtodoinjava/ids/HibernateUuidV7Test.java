package com.howtodoinjava.ids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateUuidV7Test {

  @Test
  void givenVersion7Style_whenPersisted_thenIdsAreVersion7AndAscending() {
    try (SessionFactory factory = new Configuration()
        .addAnnotatedClass(Shipment.class)
        .setProperty("hibernate.connection.url", "jdbc:h2:mem:shipments")
        .setProperty("hibernate.hbm2ddl.auto", "create-drop")
        .buildSessionFactory()) {

      Shipment first = new Shipment("DHL");
      Shipment second = new Shipment("FedEx");
      factory.inTransaction(session -> {
        session.persist(first);
        session.persist(second);
      });

      UUID a = first.getId();
      UUID b = second.getId();
      assertThat(a.version()).isEqualTo(7);
      assertThat(b.version()).isEqualTo(7);
      assertThat(a).isLessThan(b);
    }
  }

  @Test
  void givenVersion7Style_whenManyPersistedInOneTransaction_thenIdsKeepCreationOrder() {
    try (SessionFactory factory = new Configuration()
        .addAnnotatedClass(Shipment.class)
        .setProperty("hibernate.connection.url", "jdbc:h2:mem:burst")
        .setProperty("hibernate.hbm2ddl.auto", "create-drop")
        .buildSessionFactory()) {

      List<UUID> ids = new ArrayList<>();
      factory.inTransaction(session -> {
        for (int i = 0; i < 1_000; i++) {
          Shipment shipment = new Shipment("DHL");
          session.persist(shipment);
          ids.add(shipment.getId());
        }
      });

      assertThat(ids).isSorted();
    }
  }
}
