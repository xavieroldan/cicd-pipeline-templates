package com.xavieroldan.sampleapi;

import org.springframework.stereotype.Component;

// Small piece of domain logic so the pipeline has something real to test.
@Component
public class Greeter {

    public String greet(String name) {
        String who = (name == null || name.isBlank()) ? "world" : name.trim();
        return "Hello, " + who + "!";
    }
}
