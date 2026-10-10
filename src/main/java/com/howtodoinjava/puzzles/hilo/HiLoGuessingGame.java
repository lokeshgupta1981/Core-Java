package com.howtodoinjava.puzzles.hilo;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.time.*;
import java.nio.file.*;
import java.math.*;
import java.io.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;
import java.util.function.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.time.*;
import java.math.*;
import java.util.random.RandomGenerator;

/**
 * Examples for the tutorial "HiLo Guessing Game in Java: Number Guessing Program".
 * https://howtodoinjava.com/java/puzzles/hilo-guessing-game-in-java/
 */
public class HiLoGuessingGame {
    static public class HiLo {

        public static final int MIN = 1;
        public static final int MAX = 100;

        public enum Hint { HI, LO, CORRECT }

        private final int secret;
        private final int maxAttempts;
        private int attemptsUsed;
        private boolean won;

        public HiLo(int secret, int maxAttempts) {
            if (secret < MIN || secret > MAX) {
                throw new IllegalArgumentException("Secret must be between " + MIN + " and " + MAX);
            }
            this.secret = secret;
            this.maxAttempts = maxAttempts;
        }

        public static int randomSecret(RandomGenerator random) {
            return random.nextInt(MIN, MAX + 1);   // upper bound is exclusive
        }

        public Hint check(int guess) {
            if (isOver()) {
                throw new IllegalStateException("The game is over");
            }
            if (guess < MIN || guess > MAX) {
                throw new IllegalArgumentException("Guess must be between " + MIN + " and " + MAX);
            }
            attemptsUsed++;
            if (guess > secret) {
                return Hint.HI;
            }
            if (guess < secret) {
                return Hint.LO;
            }
            won = true;
            return Hint.CORRECT;
        }

        public boolean isOver() { return won || attemptsUsed >= maxAttempts; }
        public boolean isWon() { return won; }
        public int attemptsUsed() { return attemptsUsed; }
        public int attemptsLeft() { return maxAttempts - attemptsUsed; }
        public int secret() { return secret; }
    }
    static OptionalInt tryParseInt(String text) {
        try {
            return OptionalInt.of(Integer.parseInt(text.strip()));
        } catch (NumberFormatException e) {
            return OptionalInt.empty();
        }
    }
    // Returns false when the input ends before the round is over
    static boolean playRound(HiLo game, BufferedReader in, PrintStream out) throws IOException {
        while (!game.isOver()) {
            out.print("Guess (" + game.attemptsLeft() + " left): ");
            String line = in.readLine();
            if (line == null) {
                return false;
            }
            OptionalInt guess = tryParseInt(line);
            if (guess.isEmpty() || guess.getAsInt() < HiLo.MIN || guess.getAsInt() > HiLo.MAX) {
                out.println("Please type a whole number from 1 to 100.");   // does not cost an attempt
                continue;
            }
            HiLo.Hint hint = game.check(guess.getAsInt());
            switch (hint) {
                case HI -> out.println("HI");
                case LO -> out.println("LO");
                case CORRECT -> out.println("Correct! You found it in " + game.attemptsUsed() + " tries.");
            }
        }
        if (!game.isWon()) {
            out.println("No tries left. The secret number was " + game.secret() + ".");
        }
        return true;
    }
    static void play(BufferedReader in, PrintStream out, RandomGenerator random) throws IOException {
        out.println("Guess the secret number between 1 and 100. You have " + MAX_ATTEMPTS + " tries.");
        do {
            HiLo game = new HiLo(HiLo.randomSecret(random), MAX_ATTEMPTS);
            if (!playRound(game, in, out)) {
                break;   // input ended (Ctrl+D or a closed pipe)
            }
        } while (askYesNo("Play again? (y/n): ", in, out));
        out.println("Thanks for playing!");
    }

    static final int MAX_ATTEMPTS = 7;
    static boolean askYesNo(String question, BufferedReader in, PrintStream out) throws IOException {
        while (true) {
            out.print(question);
            String line = in.readLine();
            if (line == null) {
                return false;
            }
            String answer = line.strip();
            if (answer.equalsIgnoreCase("y")) {
                return true;
            }
            if (answer.equalsIgnoreCase("n")) {
                return false;
            }
            out.println("Please type y or n.");
        }
    }
    // Always guesses the middle of the numbers that are still possible
    static int solve(HiLo game) {
        int low = HiLo.MIN;
        int high = HiLo.MAX;
        while (true) {
            int guess = (low + high) >>> 1;
            HiLo.Hint hint = game.check(guess);
            switch (hint) {
                case CORRECT -> {
                    return game.attemptsUsed();
                }
                case HI -> high = guess - 1;
                case LO -> low = guess + 1;
            }
        }
    }
    public static void main(String[] args) throws Exception {
        {
            HiLo game = new HiLo(42, 7);                    // secret 42, 7 tries
            show("game", game);
            HiLo.Hint first = game.check(50);               // HI
            show("first", first);
            HiLo.Hint second = game.check(25);              // LO
            show("second", second);
            HiLo.Hint third = game.check(42);               // CORRECT
            show("third", third);
            int used = game.attemptsUsed();                 // 3
            show("used", used);
            boolean won = game.isWon();                     // true
            show("won", won);

            int secret = HiLo.randomSecret(RandomGenerator.getDefault());   // a random number from 1 to 100
            show("secret", secret);
        }
        {
            RandomGenerator random = RandomGenerator.getDefault();
            int secret = random.nextInt(1, 101);            // 1 to 100
            show("secret", secret);

            int oldStyle = new Random().nextInt(100) + 1;   // 1 to 100
            show("oldStyle", oldStyle);
            int offByOne = new Random().nextInt(100);       // 0 to 99 (never 100)
            show("offByOne", offByOne);
        }
        {
            HiLo game = new HiLo(42, 7);
            try { HiLo.Hint hint = game.check(150); show("hint", hint); } catch (Throwable _t) { System.out.println("hint -> " + _t); }

            HiLo.Hint found = game.check(42);   // CORRECT
            show("found", found);
            try { HiLo.Hint again = game.check(10); show("again", again); } catch (Throwable _t) { System.out.println("again -> " + _t); }
        }
        {
            OptionalInt valid = tryParseInt("50");      // OptionalInt[50]
            show("valid", valid);
            OptionalInt spaced = tryParseInt(" 50 ");   // OptionalInt[50]
            show("spaced", spaced);
            OptionalInt text = tryParseInt("abc");      // OptionalInt.empty
            show("text", text);
            OptionalInt big = tryParseInt("150");       // OptionalInt[150] (the range check comes next)
            show("big", big);
        }
        {
            HiLo round = new HiLo(14, 7);
            BufferedReader typed = new BufferedReader(new StringReader("abc\n150\n50\n25\n12\n18\n15\n13\n14\n"));
            boolean finished = playRound(round, typed, new PrintStream(OutputStream.nullOutputStream()));   // true
            show("finished", finished);
            int tries = round.attemptsUsed();                                                             // 7
            show("tries", tries);
            boolean roundWon = round.isWon();                                                             // true
            show("roundWon", roundWon);
        }
        {
            int most = IntStream.rangeClosed(1, 100).map(s -> solve(new HiLo(s, 7))).max().getAsInt();   // 7
            show("most", most);
            long withinSix = IntStream.rangeClosed(1, 100).filter(s -> solve(new HiLo(s, 7)) <= 6).count(); // 63
            show("withinSix", withinSix);
            int forOne = solve(new HiLo(1, 7));                                                          // 6
            show("forOne", forOne);
            int forSeventySix = solve(new HiLo(76, 7));                                                  // 6
            show("forSeventySix", forSeventySix);
        }
        {
            int secret = RandomGenerator.getDefault().nextInt(1, 101);          // 1 to 100
            show("secret", secret);
            int legacy = (int) (Math.random() * 100) + 1;                      // 1 to 100
            show("legacy", legacy);
        }
    }

    static void show(String name, Object value) {
        String text = value instanceof int[] a ? Arrays.toString(a)
        : value instanceof long[] a ? Arrays.toString(a)
        : value instanceof double[] a ? Arrays.toString(a)
        : value instanceof Object[] a ? Arrays.deepToString(a)
        : value instanceof String str ? "\"" + str + "\""
        : String.valueOf(value);
        System.out.println(name + " = " + text);
    }
}
