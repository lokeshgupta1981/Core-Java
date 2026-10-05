package com.howtodoinjava.boot;

import com.howtodoinjava.welcome.WelcomeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WelcomeApplication {

  public static void main(String[] args) {
    SpringApplication.run(WelcomeApplication.class, args);
  }

  @Bean
  CommandLineRunner welcomeRunner() {
    return args -> System.out.println(WelcomeService.welcome("Lokesh", "es"));
  }
}
