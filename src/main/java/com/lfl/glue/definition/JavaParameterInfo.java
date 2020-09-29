package com.lfl.glue.definition;

import io.cucumber.core.backend.ParameterInfo;
import io.cucumber.core.backend.TypeResolver;
import io.cucumber.java.Transpose;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class composes all interesting parameter information into one object.
 */
public class JavaParameterInfo implements ParameterInfo {

    private final Type type;
    private final boolean transposed;

    private JavaParameterInfo(Type type, boolean transposed) {
        this.type = type;
        this.transposed = transposed;
    }

    public static List<ParameterInfo> fromMethod(Method method) {
        List<ParameterInfo> result = new ArrayList<>();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < genericParameterTypes.length; i++) {
            boolean transposed = false;
            for (Annotation annotation : annotations[i]) {
                if (annotation instanceof Transpose) {
                    transposed = ((Transpose) annotation).value();
                }
            }
            result.add(new JavaParameterInfo(genericParameterTypes[i], transposed));
        }
        return result;
    }

    public Type getType() {
        return type;
    }

    public boolean isTransposed() {
        return transposed;
    }

    @Override
    public TypeResolver getTypeResolver() {
        return () -> type;
    }

}
