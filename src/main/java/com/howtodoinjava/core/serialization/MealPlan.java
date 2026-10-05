package com.howtodoinjava.core.serialization;

import java.io.Serializable;

/**
 * A serializable record with a field of a non-serializable type (MenuItem). Writing a
 * MealPlan fails with NotSerializableException: com.howtodoinjava.core.serialization.MenuItem.
 */
public record MealPlan(String day, MenuItem item) implements Serializable {
}
