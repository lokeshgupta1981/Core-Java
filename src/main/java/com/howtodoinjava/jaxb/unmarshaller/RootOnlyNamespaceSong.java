package com.howtodoinjava.jaxb.unmarshaller;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * A common mistake: the namespace is set on the root element only, so the child
 * elements are expected without a namespace and stay null or 0.
 */
@XmlRootElement(name = "song", namespace = "https://example.com/music")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootOnlyNamespaceSong {

  private String title;
  private int seconds;

  @Override
  public String toString() {
    return "RootOnlyNamespaceSong[title=" + title + ", seconds=" + seconds + "]";
  }
}
