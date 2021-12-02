package com.howtodoinjava.core.enums;

import java.util.Arrays;
import java.util.Optional;

public class EnumWithStringsExample 
{
    public static void main(String[] args) 
    {
        String url = "https://sit.domain.com:2019/";
        
        Optional<Environment> env = Environment.get(url);
         
        System.out.println(env.get());
    }
}

enum Environment 
{
    PROD("https://prod.domain.com:1088/"), 
    SIT("https://sit.domain.com:2019/"), 
    CIT("https://cit.domain.com:8080/"), 
    DEV("https://dev.domain.com:21323/");
 
    private String url;
 
    Environment(String envUrl) {
        this.url = envUrl;
    }
 
    public String getUrl() {
        return url;
    }
     
    //****** Reverse Lookup ************//
 
    public static Optional<Environment> get(String url) {
        return Arrays.stream(Environment.values())
            .filter(env -> env.url.equals(url))
            .findFirst();
    }
}