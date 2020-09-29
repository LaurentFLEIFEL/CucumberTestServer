package com.lfl.utils;

import com.lfl.utils.snippet.JavaSnippet;
import io.cucumber.core.backend.Backend;
import io.cucumber.core.backend.Glue;
import io.cucumber.core.backend.ObjectFactory;
import io.cucumber.core.backend.Snippet;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class AlreadyInstantiatedBackend implements Backend {

    private final ObjectFactory objectFactory;
    private final Map<Class<?>, Object> instancesGlue;

    public AlreadyInstantiatedBackend(ObjectFactory objectFactory, Map<Class<?>, Object> instancesGlue) {
        this.objectFactory = objectFactory;
        this.instancesGlue = instancesGlue;
    }

    @Override
    public void loadGlue(Glue glue, List<URI> gluePaths) {
        GlueAdaptor glueAdaptor = new GlueAdaptor(objectFactory, glue);
        instancesGlue.keySet().forEach(aGlueClass -> MethodScanner.scan(aGlueClass, (method, annotation) -> {
            objectFactory.addClass(method.getDeclaringClass());
            glueAdaptor.addDefinition(method, annotation);
        }));
    }

    @Override
    public void buildWorld() {

    }

    @Override
    public void disposeWorld() {

    }

    @Override
    public Snippet getSnippet() {
        return new JavaSnippet();
    }
}
