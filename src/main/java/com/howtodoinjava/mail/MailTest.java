package com.howtodoinjava.mail;

import java.util.List;

public class MailTest {

  public static void main(String[] args) {

    EmailSender.sendPlainTextEmail("admin@eprindia.in",
        "lokeshgupta1981@gmail.com",
        "Test Email",
        List.of("Hello", "World"),
        true);

    /*EmailSenderWithSSL.sendPlainTextEmail("admin@eprindia.in",
        "lokeshgupta1981@gmail.com",
        "Test Email",
        List.of("Hello", "World"),
        true);*/
  }
}
