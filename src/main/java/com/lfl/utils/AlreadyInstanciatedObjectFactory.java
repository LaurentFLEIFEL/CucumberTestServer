package com.lfl.utils;

import io.cucumber.core.backend.ObjectFactory;

import java.util.Map;

public class AlreadyInstanciatedObjectFactory implements ObjectFactory {
    private final Map<Class<?>, Object> instancesGlue;

    public AlreadyInstanciatedObjectFactory(Map<Class<?>, Object> instancesGlue) {
        this.instancesGlue = instancesGlue;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean addClass(Class<?> glueClass) {
        return true;
    }

    @Override
    public <T> T getInstance(Class<T> glueClass) {
        return glueClass.cast(instancesGlue.get(glueClass));
    }
}
