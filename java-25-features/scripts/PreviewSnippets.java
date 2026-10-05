void main() {
    int pages = 412;
    boolean inStock = true;
    String size = switch (pages) {
        case 0 -> "empty";
        case int p when p < 100 -> "short";
        case int p when p < 500 -> "medium";
        case int p -> "long (" + p + " pages)";
    };
    String label = switch (inStock) {
        case true -> "ready to borrow";
        case false -> "join the waiting list";
    };
    long copiesSold = 3_000_000_000L;
    String result = (copiesSold instanceof int copies)
        ? "fits in int: " + copies
        : "too large for int: " + copiesSold;
    boolean fits = 300 instanceof byte;
    IO.println(size + " | " + label + " | " + result + " | " + fits);
    Supplier<String> banner = StableValue.supplier(() -> "Library opened");
    IO.println(banner.get());
}
