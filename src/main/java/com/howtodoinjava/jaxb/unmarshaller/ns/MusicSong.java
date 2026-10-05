package com.howtodoinjava.jaxb.unmarshaller.ns;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * The song from the parent package, mapped to namespace-qualified XML through package-info.java.
 */
@XmlRootElement(name = "song")
@XmlAccessorType(XmlAccessType.FIELD)
public class MusicSong {

  private String title;
  private int seconds;

  @Override
  public String toString() {
    return "MusicSong[title=" + title + ", seconds=" + seconds + "]";
  }
}
