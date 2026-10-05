package com.howtodoinjava.core.enums;

import java.util.function.UnaryOperator;

public enum Turn implements UnaryOperator<Direction> {

  LEFT {
    @Override
    public Direction apply(Direction current) {
      return rotate(current, 90);
    }
  },

  RIGHT {
    @Override
    public Direction apply(Direction current) {
      return rotate(current, -90);
    }
  },

  BACK {
    @Override
    public Direction apply(Direction current) {
      return rotate(current, 180);
    }
  };

  // Every constant must implement this method
  @Override
  public abstract Direction apply(Direction current);

  private static Direction rotate(Direction current, int degrees) {
    return Direction.fromAngle(current.getAngle() + degrees).orElseThrow();
  }
}
