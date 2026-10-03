package com.howtodoinjava.jaxb.unmarshaller;

import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * A song mapped to the root element {@code <song>}. JAXB needs the public no-arg
 * constructor; the two callback methods show when the unmarshaller calls them.
 */
@XmlRootElement(name = "song")
@XmlAccessorType(XmlAccessType.FIELD)
public class Song {

  static boolean printCallbacks = false;

  @XmlAttribute
  private int id;

  private String title;

  @XmlElement(defaultValue = "Unknown")
  private String artist;

  private int seconds;

  public Song() {
  }

  public int getId() { return id; }
  public String getTitle() { return title; }
  public String getArtist() { return artist; }
  public int getSeconds() { return seconds; }

  // Called after the object is created and before its fields are read
  void beforeUnmarshal(Unmarshaller unmarshaller, Object parent) {
    if (printCallbacks) {
      System.out.println("beforeUnmarshal: " + this);
    }
  }

  // Called after all fields are read
  void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
    if (printCallbacks) {
      System.out.println("afterUnmarshal: " + this);
    }
  }

  @Override
  public String toString() {
    return "Song[id=" + id + ", title=" + title + ", artist=" + artist + ", seconds=" + seconds + "]";
  }
}
