package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * The root class that holds the list. {@code @XmlElementWrapper} writes an
 * {@code <ingredients>} element around the {@code <ingredient>} items.
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class Recipe {

  @XmlAttribute
  private String name;

  @XmlElementWrapper(name = "ingredients")
  @XmlElement(name = "ingredient")
  private List<Ingredient> ingredients = new ArrayList<>();

  public Recipe() {
  }

  public Recipe(String name) {
    this.name = name;
  }

  public String getName() { return name; }
  public List<Ingredient> getIngredients() { return ingredients; }
  public void setIngredients(List<Ingredient> ingredients) { this.ingredients = ingredients; }

  @Override
  public String toString() {
    return "Recipe[name=" + name + ", ingredients=" + ingredients + "]";
  }
}
