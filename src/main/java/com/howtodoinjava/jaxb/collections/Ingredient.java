package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * One item of a recipe. The {@code @XmlRootElement} is needed only for the generic
 * {@link Wrapper}, which finds the class of each item by its element name.
 */
@XmlRootElement(name = "ingredient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ingredient {

  @XmlAttribute
  private String name;

  @XmlAttribute
  private int grams;

  public Ingredient() {
  }

  public Ingredient(String name, int grams) {
    this.name = name;
    this.grams = grams;
  }

  public String getName() { return name; }
  public int getGrams() { return grams; }

  @Override
  public String toString() {
    return name + " " + grams + "g";
  }
}
