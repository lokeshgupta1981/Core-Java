static final ScopedValue<String> MEMBER = ScopedValue.newInstance();

void main() {
    ScopedValue.where(MEMBER, "Lokesh").run(() -> greet());
    boolean bound = MEMBER.isBound();
    IO.println("Bound after run(): " + bound);
}

void greet() {
    IO.println("Hello, " + MEMBER.get());
}
