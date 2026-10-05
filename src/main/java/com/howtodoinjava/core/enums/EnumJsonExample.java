package com.howtodoinjava.core.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class EnumJsonExample {

  record Move(Direction direction, int steps) {
  }

  enum CupSize {
    SMALL("S"), LARGE("L"),
    @JsonEnumDefaultValue UNKNOWN("?");

    private final String code;

    CupSize(String code) {
      this.code = code;
    }

    @JsonValue
    public String getCode() {
      return code;
    }
  }

  public static void main(String[] args) throws Exception {
    JsonMapper mapper = JsonMapper.builder().build();

    String json = mapper.writeValueAsString(new Move(Direction.NORTH, 3)); // {"direction":"NORTH","steps":3}
    Move move = mapper.readValue(json, Move.class);                       // Move[direction=NORTH, steps=3]
    String sizeJson = mapper.writeValueAsString(CupSize.LARGE);            // "L"
    CupSize size = mapper.readValue("\"S\"", CupSize.class);              // SMALL
    System.out.println(json + " " + move + " " + sizeJson + " " + size);

    try {
      Direction bad = mapper.readValue("\"UP\"", Direction.class);
      System.out.println(bad);
    } catch (InvalidFormatException e) {
      System.out.println(e.getOriginalMessage());
    }

    JsonMapper lenient = JsonMapper.builder()
        .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
        .build();
    CupSize fallback = lenient.readValue("\"XL\"", CupSize.class);       // UNKNOWN
    System.out.println(fallback);
  }
}
