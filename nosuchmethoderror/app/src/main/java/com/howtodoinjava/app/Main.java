package com.howtodoinjava.app;

import com.howtodoinjava.report.ReportHeader;
import com.howtodoinjava.welcome.WelcomeService;

public class Main {

  public static void main(String[] args) {
    System.out.println(ReportHeader.header("Lokesh"));
    System.out.println(WelcomeService.welcome("Lokesh", "es"));
  }
}
