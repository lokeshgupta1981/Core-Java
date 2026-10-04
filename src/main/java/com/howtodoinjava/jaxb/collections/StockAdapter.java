package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlValue;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Converts a {@code Map<String, Integer>} to a list of {@code <item name="..">value</item>}
 * elements and back.
 */
public class StockAdapter extends XmlAdapter<StockAdapter.Items, Map<String, Integer>> {

  public static class Items {
    @XmlElement(name = "item")
    public List<Item> list = new ArrayList<>();
  }

  public static class Item {
    @XmlAttribute
    public String name;
    @XmlValue
    public int count;
  }

  @Override
  public Items marshal(Map<String, Integer> map) {
    Items items = new Items();
    map.forEach((name, count) -> {
      Item item = new Item();
      item.name = name;
      item.count = count;
      items.list.add(item);
    });
    return items;
  }

  @Override
  public Map<String, Integer> unmarshal(Items items) {
    Map<String, Integer> map = new TreeMap<>();
    items.list.forEach(item -> map.put(item.name, item.count));
    return map;
  }
}
