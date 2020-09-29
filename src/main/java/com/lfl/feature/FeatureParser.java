package com.lfl.feature;

import io.cucumber.core.gherkin.Feature;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.UUID;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

public final class FeatureParser {

    private final Supplier<UUID> idGenerator;

    public FeatureParser(Supplier<UUID> idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Optional<Feature> parseResource(String source) {
        requireNonNull(source);
        ServiceLoader<io.cucumber.core.gherkin.FeatureParser> services = ServiceLoader
                .load(io.cucumber.core.gherkin.FeatureParser.class);
        Iterator<io.cucumber.core.gherkin.FeatureParser> iterator = services.iterator();
        List<io.cucumber.core.gherkin.FeatureParser> parser = new ArrayList<>();
        while (iterator.hasNext()) {
            parser.add(iterator.next());
        }
        Comparator<io.cucumber.core.gherkin.FeatureParser> version = comparing(
                io.cucumber.core.gherkin.FeatureParser::version);
        URI path = null;
        try {
            path = new URI("");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Collections.max(parser, version).parse(path, source, idGenerator);
    }

}
