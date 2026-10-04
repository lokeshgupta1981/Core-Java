package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * A Set field without an initial value: on unmarshal JAXB creates a HashSet,
 * so the order of the XML elements is lost.
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlainTaggedRecipe {

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  private Set<String> tags;

  @Override
  public String toString() {
    return "PlainTaggedRecipe[tags=" + tags + ", class=" + tags.getClass().getSimpleName() + "]";
  }
}
