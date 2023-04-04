package com.howtodoinjava.core.enums;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class EnumExamples {

  public static void main(String[] args) {
    Direction north = Direction.NORTH;
    System.out.println(north);        //Prints NORTH

    Direction.EAST.ordinal();     //0
    Direction.NORTH.ordinal();    //2

    Direction[] directions = Direction.values();
    for (Direction d : directions) {
      System.out.println(d);
    }

    Direction west = Direction.WEST;
    System.out.println(west);
    System.out.println(west.getAngle());

    //or

    System.out.println(Direction.WEST.getAngle());

    System.out.println(Direction.WEST.message());
    System.out.println(Direction.NORTH.message());

    Set enumSet = EnumSet.of(Direction.EAST,
        Direction.WEST,
        Direction.NORTH,
        Direction.SOUTH
    );

    Map<Direction, Integer> enumMap = new EnumMap(Direction.class);

    enumMap.put(Direction.EAST, Direction.EAST.getAngle());
    enumMap.put(Direction.WEST, Direction.WEST.getAngle());
    enumMap.put(Direction.NORTH, Direction.NORTH.getAngle());
    enumMap.put(Direction.SOUTH, Direction.SOUTH.getAngle());

    PathUtils pathUtils = PathUtils.getInstance();
  }
}



enum Direction {
  EAST(0), WEST(180), NORTH(90), SOUTH(270);

  // constructor
  private Direction(final int angle) {
    this.angle = angle;
  }

  // internal state
  private int angle;

  public int getAngle() {
    return angle;
  }

  protected String message() {

    String message = "Moving in " + this + " direction";
    return message;
  }
}