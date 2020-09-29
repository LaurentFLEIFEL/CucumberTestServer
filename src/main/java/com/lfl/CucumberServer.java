package com.lfl;

import com.lfl.feature.FeatureParser;
import com.lfl.feature.ParsingFeatureSupplier;
import com.lfl.utils.AlreadyInstanciatedObjectFactory;
import com.lfl.utils.AlreadyInstantiatedBackend;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.eventbus.EventBus;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.options.RuntimeOptionsBuilder;
import io.cucumber.core.resource.ClassLoaders;
import io.cucumber.core.runtime.BackendSupplier;
import io.cucumber.core.runtime.FeatureSupplier;
import io.cucumber.core.runtime.ObjectFactorySupplier;
import io.cucumber.core.runtime.TimeServiceEventBus;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CucumberServer {
    public byte runCucumberTest(Set<String> featureSources, Set<Object> stepDefinitionInstances) {
        return runCucumberTest(featureSources, stepDefinitionInstances, "target/cucumber.json");
    }

    public byte runCucumberTest(Set<String> featureSources, Set<Object> stepDefinitionInstances, String jsonOutputPath) {
        final Map<Class<?>, Object> instancesGlue = stepDefinitionInstances.stream()
                                                                           .collect(Collectors.toMap(Object::getClass, instance -> instance));

        Supplier<ClassLoader> classLoader = ClassLoaders::getDefaultClassLoader;
        RuntimeOptions runtimeOptions = new RuntimeOptionsBuilder().addPluginName("json:" + jsonOutputPath).addPluginName("pretty").build();

        EventBus eventBus = new TimeServiceEventBus(Clock.systemUTC(), UUID::randomUUID);

        final ObjectFactorySupplier objectFactorySupplier = () -> new AlreadyInstanciatedObjectFactory(instancesGlue);

        ObjectFactory objectFactory = objectFactorySupplier.get();

        final BackendSupplier backendSupplier = () -> List.of(new AlreadyInstantiatedBackend(objectFactory, instancesGlue));

        final FeatureParser parser = new FeatureParser(eventBus::generateId);

        final FeatureSupplier featureSupplier = new ParsingFeatureSupplier(featureSources, parser);


        CucumberRunner runner = CucumberRunner.builder()
                                              .withBackendSupplier(backendSupplier)
                                              .withFeatureSupplier(featureSupplier)
                                              .withObjectFactorySupplier(objectFactorySupplier)
                                              .withEventBus(eventBus)
                                              .withRuntimeOptions(runtimeOptions)
                                              .withClassLoader(classLoader)
                                              .build();

        runner.run();
        return runner.exitStatus();
    }
}
