package io.cucumber.java;

import io.cucumber.core.backend.TestCaseState;

public class ScenarioHelper {

    public static Scenario buildScenario(TestCaseState state) {
        return new Scenario(state);
    }
}
