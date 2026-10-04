package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The same recipe without {@code @XmlElementWrapper}: the {@code <ingredient>}
 * items are written directly under {@code <recipe>}.
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlatRecipe {

  @XmlAttribute
  private String name;

  @XmlElement(name = "ingredient")
  private List<Ingredient> ingredients = new ArrayList<>();

  public FlatRecipe() {
  }

  public FlatRecipe(String name) {
    this.name = name;
  }

  public List<Ingredient> getIngredients() { return ingredients; }
  public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

  @Override
  public String toString() {
    return "FlatRecipe[name=" + name + ", ingredients=" + ingredients + "]";
  }
}
