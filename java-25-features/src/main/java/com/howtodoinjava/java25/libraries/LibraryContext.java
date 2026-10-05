package com.howtodoinjava.java25.libraries;

import java.util.ArrayList;
import java.util.List;

/** JEP 506: scoped values. */
public class LibraryContext {

  public static final ScopedValue<String> MEMBER = ScopedValue.newInstance();

  private final List<String> log = new ArrayList<>();

  public void borrow(String member, String title) {
    ScopedValue.where(MEMBER, member).run(() -> checkout(title));
  }

  private void checkout(String title) {
    String who = MEMBER.get();
    log.add(who + " borrowed " + title);
    ScopedValue.where(MEMBER, "Librarian").run(() -> stamp(title));
    String after = MEMBER.get();
    log.add(after + " left with " + title);
  }

  private void stamp(String title) {
    log.add(MEMBER.get() + " stamped " + title);
  }

  public static int memberDiscount() throws Exception {
    return ScopedValue.where(MEMBER, "Lokesh").call(() -> MEMBER.get().length() * 2);
  }

  public static String currentMemberOrGuest() {
    return MEMBER.orElse("guest");
  }

  public List<String> log() {
    return log;
  }
}
