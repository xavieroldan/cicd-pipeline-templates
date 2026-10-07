package com.xavieroldan.sampleapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GreeterTest {

    private final Greeter greeter = new Greeter();

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"Xavi|Hello, Xavi!", "'  Ana '|Hello, Ana!", "NULL|Hello, world!"}, nullValues = "NULL")
    void greetReturnsExpectedMessage(String name, String expected) {
        assertEquals(expected, greeter.greet(name));
    }
}
