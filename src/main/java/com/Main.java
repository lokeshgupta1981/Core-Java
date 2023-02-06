package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.harmony.pack200.NewAttributeBands.Call;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  public static void main(String[] asrg) {

    float PI = 3.1415927f;
    float negativePI = -3.1415927f;

    //1
    Assertions.assertEquals("3.1415927", Float.toString(PI));
    Assertions.assertEquals("-3.1415927", Float.toString(negativePI));

    Assertions.assertEquals("NaN", Float.toString(0.0f / 0.0f));
    Assertions.assertEquals("Infinity", Float.toString(Float.POSITIVE_INFINITY));
    Assertions.assertEquals("-Infinity", Float.toString(Float.NEGATIVE_INFINITY));

    //2
    Assertions.assertEquals("3.1415927", String.valueOf(PI));
    Assertions.assertEquals("-3.1415927", String.valueOf(negativePI));

    //3
    NumberFormat formatter = new DecimalFormat("0.00");
    Assertions.assertEquals("3.14", formatter.format(PI));

  }
}
