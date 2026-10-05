package com.howtodoinjava.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;

class RecipeServiceTest {

  @Test
  void returnsOnlyQuickRecipes() {
    // Mockito creates the mock with Byte Buddy, so this test fails when Byte Buddy is too old for the JDK
    RecipeRepository repository = mock(RecipeRepository.class);
    when(repository.findAll()).thenReturn(List.of(
        Recipe.builder().name("omelette").minutes(10).build(),
        Recipe.builder().name("lasagna").minutes(90).build(),
        Recipe.builder().name("salad").minutes(5).build()));

    RecipeService service = new RecipeService(repository);

    assertEquals(List.of("omelette", "salad"), service.quickRecipes(15));
  }

  @Test
  void printsTheRuntimeVersion() {
    int feature = Runtime.version().feature();
    System.out.println("Tests running on Java " + feature);
    assertTrue(feature >= 21);
  }
}
