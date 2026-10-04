package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A recipe with a {@code Set} of tags. The XML order of the tags is the iteration
 * order of the Set implementation; the initialized LinkedHashSet keeps the
 * XML order when JAXB reads the tags back.
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaggedRecipe {

  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tag")
  private Set<String> tags = new LinkedHashSet<>();   // keeps the XML order on unmarshal

  public Set<String> getTags() { return tags; }
  public void setTags(Set<String> tags) { this.tags = tags; }

  @Override
  public String toString() {
    return "TaggedRecipe[tags=" + tags + "]";
  }
}
