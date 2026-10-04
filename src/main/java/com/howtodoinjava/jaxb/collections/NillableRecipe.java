package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A wrapper with {@code nillable = true}: a null list is written as
 * {@code <ingredients xsi:nil="true"/>} instead of being left out.
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class NillableRecipe {

  @XmlElementWrapper(name = "ingredients", nillable = true)
  @XmlElement(name = "ingredient")
  private List<Ingredient> ingredients;

  public List<Ingredient> getIngredients() { return ingredients; }
  public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

  @Override
  public String toString() {
    return "NillableRecipe[ingredients=" + ingredients + "]";
  }
}
