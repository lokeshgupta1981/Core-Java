package com;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
        log.debug("Sample debug message");
        log.info("Sample info message");
        log.error("Sample error message");
    }
}
