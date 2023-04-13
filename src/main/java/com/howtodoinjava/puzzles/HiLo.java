package com.howtodoinjava.puzzles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class HiLo {

  private final Random generator;
  private int generatedNumber;
  private int numberOfAttempts;
  BufferedReader reader = null;

  public HiLo() {
    generator = new Random();
    reader = new BufferedReader(new InputStreamReader(System.in));
  }

  public void start() throws IOException {

    boolean wantToPlay = false;
    boolean firstTime = true;

    do {
      System.out.println("\n\nWant to play the game of Hi and Lo??");

      if (wantToPlay = prompt()) {
        generatedNumber = generateSecretNumber();
        numberOfAttempts = 0;
        if (firstTime) {
          describeRules();
          firstTime = false;
        }
        playGame();
      }

    } while (wantToPlay);

    System.out.println("\nThanks for playing the game. Hope you loved it !!");
    reader.close();
  }

  private void describeRules() {

    System.out.println("\nOnly 2 Rules:");
    System.out.println("1) Guess the secret number in maximum 6 tries.");
    System.out.println("2) The secret number is an integer between 1 and 100, inclusive :-)\n\n");
  }

  private int generateSecretNumber() {
    return (generator.nextInt(100) + 1);
  }

  private void playGame() throws IOException {

    while (numberOfAttempts < 6) {

      int guess = getNextGuess();

      if (guess > generatedNumber) {
        System.out.println("HI");
      } else if (guess < generatedNumber) {
        System.out.println("LO");
      } else {
        System.out.println("Brave Soul, You guessed the right number!! Congratulations !!");
        return;
      }
      numberOfAttempts++;
    }

    System.out.println("Sorry, you didn't guess the right number in six attempts !!");
    System.out.println("The secret number was " + generatedNumber);

  }

  private boolean prompt() {

    boolean answer = false;

    try {

      boolean inputOk = false;
      while (!inputOk) {
        System.out.print("Y / N : ");
        String input = reader.readLine();
        if (input.equalsIgnoreCase("y")) {
          inputOk = true;
          answer = true;
        } else if (input.equalsIgnoreCase("n")) {
          inputOk = true;
          answer = false;
        } else {
          System.out.println(
              "Ohh come on. Even Mr. Bean knows where are 'y' and 'n' in the keyboard?? Please try again:");
        }
      }

    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    return answer;
  }

  private int getNextGuess() throws IOException {

    boolean inputOk = false;
    int number = 0;
    String input = null;
    while (!inputOk) {
      try {

        System.out.print("Please guess the secret number: ");
        input = reader.readLine();
        number = Integer.parseInt(input);
        if (number >= 1 && number <= 100) {
          inputOk = true;
        } else {
          System.out.println(
              "Really? You didn't read the rules boy. Your number is not between 1 and 100 ("
                  + number + ").");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input (" + input + ")");
      }
    }

    return number;
  }
}
