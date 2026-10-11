package com.howtodoinjava.core.enums;

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

/**
 * Examples for the tutorial "Java Enum with Multiple Values: Fields and Reverse Lookup".
 * https://howtodoinjava.com/java/enum/enum-with-multiple-values/
 */
public class EnumMultipleValuesLookup {
    static public enum AccountStatus {

        PURGED("P", "purged", -1),
        INACTIVE("I", "inactive", 0),
        ACTIVE("A", "active", 1);

        private static final Map<String, AccountStatus> BY_NAME = new HashMap<>();
        private static final Map<Integer, AccountStatus> BY_CODE = new HashMap<>();

        static {
            for (AccountStatus status : values()) {
                BY_NAME.put(status.shortName.toLowerCase(Locale.ROOT), status);
                BY_NAME.put(status.fullName.toLowerCase(Locale.ROOT), status);
                BY_CODE.put(status.code, status);
            }
        }

        private final String shortName;
        private final String fullName;
        private final int code;

        AccountStatus(String shortName, String fullName, int code) {
            this.shortName = shortName;
            this.fullName = fullName;
            this.code = code;
        }

        public String getShortName() {
            return shortName;
        }

        public String getFullName() {
            return fullName;
        }

        public int getCode() {
            return code;
        }

        public static Optional<AccountStatus> fromValue(String value) {
            if (value == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(BY_NAME.get(value.strip().toLowerCase(Locale.ROOT)));
        }

        public static Optional<AccountStatus> fromCode(int code) {
            return Optional.ofNullable(BY_CODE.get(code));
        }
    }
    static Optional<AccountStatus> findByCode(int code) {
        return Arrays.stream(AccountStatus.values())
                .filter(status -> status.getCode() == code)
                .findFirst();
    }
    static public enum PaymentMethod {

        CARD("card", "credit", "debit"),
        BANK_TRANSFER("bank", "transfer"),
        CASH("cash");

        private static final Map<String, PaymentMethod> BY_ALIAS = new HashMap<>();

        static {
            for (PaymentMethod method : values()) {
                for (String alias : method.aliases) {
                    PaymentMethod previous = BY_ALIAS.putIfAbsent(alias, method);
                    if (previous != null) {
                        throw new IllegalStateException("Alias " + alias + " is used by " + previous + " and " + method);
                    }
                }
            }
        }

        private final List<String> aliases;

        PaymentMethod(String... aliases) {
            this.aliases = List.of(aliases);
        }

        public List<String> getAliases() {
            return aliases;
        }

        public static Optional<PaymentMethod> fromAlias(String alias) {
            return alias == null ? Optional.empty() : Optional.ofNullable(BY_ALIAS.get(alias.strip().toLowerCase(Locale.ROOT)));
        }
    }
    static public enum Priority {

        LOW(1),
        HIGH(10, "Urgent");

        private final int weight;
        private final String label;

        Priority(int weight) {
            this(weight, "Normal");
        }

        Priority(int weight, String label) {
            this.weight = weight;
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
    public static void main(String[] args) throws Exception {
        {
            AccountStatus status = AccountStatus.ACTIVE;
            String shortName = status.getShortName();                              // "A"
            show("shortName", shortName);
            String fullName = status.getFullName();                                // "active"
            show("fullName", fullName);
            int code = status.getCode();                                           // 1
            show("code", code);
            AccountStatus byCode = AccountStatus.fromCode(0).orElseThrow();        // INACTIVE
            show("byCode", byCode);
            AccountStatus byName = AccountStatus.fromValue("P").orElseThrow();     // PURGED
            show("byName", byName);
            Optional<AccountStatus> unknown = AccountStatus.fromValue("X");        // Optional.empty
            show("unknown", unknown);
        }
        {
            for (AccountStatus as : AccountStatus.values()) {
                System.out.println("Status " + as.getCode() + " is " + as.getFullName() + " (" + as.getShortName() + ")");
            }
        }
        {
            String constantName = AccountStatus.ACTIVE.name();          // "ACTIVE"
            show("constantName", constantName);
            String displayName = AccountStatus.ACTIVE.getFullName();     // "active"
            show("displayName", displayName);
            int position = AccountStatus.ACTIVE.ordinal();               // 2
            show("position", position);
        }
        {
            Optional<AccountStatus> inactive = findByCode(0);      // Optional[INACTIVE]
            show("inactive", inactive);
            Optional<AccountStatus> missing = findByCode(7);       // Optional.empty
            show("missing", missing);
        }
        {
            AccountStatus trimmed = AccountStatus.fromValue(" Active ").orElseThrow();                  // ACTIVE
            show("trimmed", trimmed);
            AccountStatus fallback = AccountStatus.fromValue("closed").orElse(AccountStatus.INACTIVE);     // INACTIVE
            show("fallback", fallback);
            Optional<AccountStatus> missing = AccountStatus.fromValue(null);                               // Optional.empty
            show("missing", missing);
            try { AccountStatus strict = AccountStatus.fromValue("closed").orElseThrow(() -> new IllegalArgumentException("Unknown status")); show("strict", strict); } catch (Throwable _t) { System.out.println("strict -> " + _t); }
        }
        {
            AccountStatus byConstantName = AccountStatus.valueOf("ACTIVE");    // ACTIVE
            show("byConstantName", byConstantName);
            try { AccountStatus wrongCase = AccountStatus.valueOf("active"); show("wrongCase", wrongCase); } catch (Throwable _t) { System.out.println("wrongCase -> " + _t); }
        }
        {
            List<String> cardAliases = PaymentMethod.CARD.getAliases();               // [card, credit, debit]
            show("cardAliases", cardAliases);
            PaymentMethod debit = PaymentMethod.fromAlias("Debit").orElseThrow();        // CARD
            show("debit", debit);
            PaymentMethod transfer = PaymentMethod.fromAlias("transfer").orElseThrow();  // BANK_TRANSFER
            show("transfer", transfer);
        }
        {
            String lowLabel = Priority.LOW.getLabel();      // "Normal"
            show("lowLabel", lowLabel);
            String highLabel = Priority.HIGH.getLabel();    // "Urgent"
            show("highLabel", highLabel);
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
