package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAnyElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic holder for a list of any {@code @XmlRootElement} class. Marshal it inside a
 * {@code JAXBElement} to choose the root element name at runtime.
 */
public class Wrapper<T> {

  @XmlAnyElement(lax = true)
  private List<T> items = new ArrayList<>();

  public Wrapper() {
  }

  public Wrapper(List<T> items) {
    this.items = items;
  }

  public List<T> getItems() { return items; }
}
