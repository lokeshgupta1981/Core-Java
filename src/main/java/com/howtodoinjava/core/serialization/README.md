# Java Serialization

Source code for the article [Java Serialization with Serializable and serialVersionUID](https://howtodoinjava.com/java/serialization/java-serialization/).

- `JavaSerializationDemo`: runs every example of the article and prints each result (writeObject/readObject, shared references, NotSerializableException, transient and static fields, serialVersionUID mismatch, records, custom readObject(), Externalizable, a non-serializable parent, readResolve() for singletons, deserialization filters).
- Model classes: `Recipe` (record), `RecipeCard`, `Cookbook`, `Ingredient` (Externalizable), `MenuItem`, `Dessert`, `MenuItemNoDefault`, `Drink`, `MealPlan`, `KitchenSettings`, `KitchenSettingsNoResolve`.
- `SerializationUtil`: helpers to serialize to a byte array and back, with an optional `ObjectInputFilter`.

Java 21 target (runs on Java 21 and newer).

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.serialization.JavaSerializationDemo
```

Other examples in this folder:

- `DeepCopyDemo` and `TestClass`: deep copy of an object with in-memory serialization.
- `SerializeObjectToString`: serialize an object to a String with Java serialization, Jackson and Gson.
