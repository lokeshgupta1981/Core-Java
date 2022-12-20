package com.howtodoinjava.core.basic.record;

import io.soabase.recordbuilder.core.RecordBuilder;

public class BuilderExample {

  public static void main(String[] args) {

    User user = UserBuilder.builder()
        .id(1l)
        .name("Lokesh")
        .email("email@domain.com")
        .status(true)
        .build();

    System.out.println(user);

    User inactiveUser = UserBuilder.builder(user)
        .email("new-email@domain.com")
        .status(true)
        .build();

    System.out.println(inactiveUser);
  }
}


//@lombok.Builder
@RecordBuilder
record User(long id, String name, String email, boolean status) {

 /* public static final class Builder {

    long id;
    String name;
    String email;
    boolean status;

    public Builder(long id, String name) {
      this.id = id;
      this.name = name;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder status(boolean status) {
      this.status = status;
      return this;
    }

    public User build() {
      return new User(id, name, email, status);
    }
  }*/
}
