package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;
import java.util.TreeMap;

/**
 * A Map with {@link StockAdapter}: each entry becomes {@code <item name="apple">5</item>}.
 */
@XmlRootElement(name = "pantry")
@XmlAccessorType(XmlAccessType.FIELD)
public class AdaptedPantry {

  @XmlJavaTypeAdapter(StockAdapter.class)
  private Map<String, Integer> stock = new TreeMap<>();

  public Map<String, Integer> getStock() { return stock; }

  @Override
  public String toString() {
    return "AdaptedPantry[stock=" + stock + "]";
  }
}
