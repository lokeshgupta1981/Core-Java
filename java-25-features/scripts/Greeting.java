void main() {
    String name = IO.readln("Your name: ");
    List<String> books = List.of("Dune", "Emma", "Ulysses");
    IO.println("Hello " + name + ", we have " + books.size() + " books");
}
