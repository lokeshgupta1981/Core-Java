# Object Cloning in Java

Source code for the article [How to Clone an Object in Java: Shallow vs Deep Copy](https://howtodoinjava.com/java/cloning/a-guide-to-object-cloning-in-java/).

- Java 21 bytecode, tested on JDK 25. Commons Lang and Jackson come from the root `pom.xml`.
- `CloningDemo` prints every result shown in the article: `clone()`, copy constructors, shallow and deep copies,
  arrays, collections, serialization, `SerializationUtils.clone()`, Jackson and records.
- Classes: `Song`, `LiveSong`, `ShallowPlaylist`, `Playlist`, `Album`, `Podcast`, `Track`, `DeepCopyUtils`.
- `DeepCloningExample` is the older Employee/Department example.

Run:

```bash
mvn -q compile exec:java -Dexec.mainClass=com.howtodoinjava.cloning.CloningDemo
```
