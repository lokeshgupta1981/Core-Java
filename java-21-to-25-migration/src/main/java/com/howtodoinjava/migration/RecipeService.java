package com.howtodoinjava.migration;

import java.util.List;

public class RecipeService {

  private final RecipeRepository repository;

  public RecipeService(RecipeRepository repository) {
    this.repository = repository;
  }

  /** Returns the names of the recipes that are ready in the given number of minutes or less. */
  public List<String> quickRecipes(int maxMinutes) {
    return repository.findAll().stream()
        .filter(recipe -> recipe.getMinutes() <= maxMinutes)
        .map(Recipe::getName)
        .toList();
  }
}
