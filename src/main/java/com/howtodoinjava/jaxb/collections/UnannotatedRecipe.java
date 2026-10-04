package com.howtodoinjava.jaxb.collections;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * A List field without any annotation: JAXB uses the field name, so every item
 * is written as an {@code <ingredients>} element.
 */
@XmlRootElement(name = "recipe")
@XmlAccessorType(XmlAccessType.FIELD)
public class UnannotatedRecipe {

  private List<Ingredient> ingredients = new ArrayList<>();

  public List<Ingredient> getIngredients() { return ingredients; }
}
