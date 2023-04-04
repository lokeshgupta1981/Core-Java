package com.howtodoinjava.core.enums;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum PathUtils {
  INSTANCE;

  public static PathUtils getInstance() {
    return INSTANCE;
  }

  PathUtils() {
    rootPath = Paths.get("");
  }

  private final Path rootPath;

  public Path getRootPath() {
    return rootPath;
  }
}
