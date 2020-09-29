package com.lfl.feature;

import io.cucumber.core.gherkin.Feature;
import io.cucumber.core.runtime.FeatureSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ParsingFeatureSupplier implements FeatureSupplier {

    private final Set<String> featureSources;
    private final FeatureParser parser;

    public ParsingFeatureSupplier(Set<String> featureSources, FeatureParser parser) {
        Objects.requireNonNull(featureSources);
        this.featureSources = featureSources;
        this.parser = parser;
    }

    @Override
    public List<Feature> get() {
        return featureSources.stream()
                             .map(parser::parseResource)
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .collect(Collectors.toCollection(() -> new ArrayList<>(featureSources.size())));
    }
}
