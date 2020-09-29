package com.lfl;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class TestStepDefinition {
    @Given("that it works")
    public void thatItWorks() {

    }

    @Then("print {string}")
    public void print(String toDisplay) {
        System.out.println(toDisplay);
    }
}
