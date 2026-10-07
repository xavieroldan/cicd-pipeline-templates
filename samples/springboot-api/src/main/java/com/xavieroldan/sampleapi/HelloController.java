package com.xavieroldan.sampleapi;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final Greeter greeter;

    public HelloController(Greeter greeter) {
        this.greeter = greeter;
    }

    @GetMapping("/hello")
    public Map<String, String> hello(@RequestParam(required = false) String name) {
        return Map.of("message", greeter.greet(name));
    }
}
