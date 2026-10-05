// A compact source file: no class, no imports, java.base is imported implicitly
void main() {
    List<String> fruits = List.of("apple", "banana");
    Map<String, Integer> lengths = fruits.stream()
        .collect(Collectors.toMap(f -> f, String::length));
    IO.println(lengths);
}
