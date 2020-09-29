package com.lfl;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Set;

public class CucumberServerTest {

    @Test
    public void run_simple_cucumber_feature() {
        //Given
        Set<String> featureSources = Set.of("Feature: Test\n" +
                                            "    \n" +
                                            "    Scenario: test\n" +
                                            "        Given that it works\n" +
                                            "        Then print \"toto\"");
        Set<Object> instances = Set.of(new TestStepDefinition());

        //When
        CucumberServer cucumberServer = new CucumberServer();

        //Then
        Assertions.assertThatNoException().isThrownBy(() -> cucumberServer.runCucumberTest(featureSources, instances));
    }
}