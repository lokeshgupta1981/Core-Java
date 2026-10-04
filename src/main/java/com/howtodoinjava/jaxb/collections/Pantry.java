package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Map without an adapter: the JAXB reference implementation writes
 * {@code <entry><key>..</key><value>..</value></entry>} items.
 */
@XmlRootElement(name = "pantry")
@XmlAccessorType(XmlAccessType.FIELD)
public class Pantry {

  private Map<String, Integer> stock = new TreeMap<>();

  public Map<String, Integer> getStock() { return stock; }

  @Override
  public String toString() {
    return "Pantry[stock=" + stock + "]";
  }
}
