# Related Tutorials on [howtodoinjava.com](https://howtodoinjava.com/)

* [Merge Two Lists](https://howtodoinjava.com/java/collections/arraylist/merge-arraylists/)
* [Correct Way to Assert Two Equal Lists Ignoring Order](https://howtodoinjava.com/java/collections/arraylist/assert-two-equal-lists-ignoring-order/)
* [Serialize and Deserialize an ArrayList in Java](https://howtodoinjava.com/java/collections/arraylist/serialize-deserialize-arraylist/)

## Serialize and Deserialize an ArrayList

Source code for the article [Serialize and Deserialize an ArrayList in Java](https://howtodoinjava.com/java/collections/arraylist/serialize-deserialize-arraylist/).

- `SerializationDeserialization`: file and byte-array round trips, the unchecked cast, other List classes, and an `ObjectInputFilter`.
- `Employee`: the serializable element type (also used by `RemoveIf`).

Java 21 bytecode, tested on JDK 25. Run with:

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.core.collections.list.SerializationDeserialization
```
