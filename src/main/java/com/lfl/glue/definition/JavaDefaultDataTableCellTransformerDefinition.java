package com.lfl.glue.definition;

import io.cucumber.core.backend.DefaultDataTableCellTransformerDefinition;
import io.cucumber.core.backend.Lookup;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.java.DefaultDataTableCellTransformer;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JavaDefaultDataTableCellTransformerDefinition extends AbstractDatatableElementTransformerDefinition
        implements DefaultDataTableCellTransformerDefinition {

    private final TableCellByTypeTransformer transformer;

    public JavaDefaultDataTableCellTransformerDefinition(Method method, Lookup lookup, String[] emptyPatterns) {
        super(requireValidMethod(method), lookup, emptyPatterns);
        this.transformer = (cellValue, toValueType) -> invokeMethod(replaceEmptyPatternsWithEmptyString(cellValue),
                                                                    toValueType);
    }

    private static Method requireValidMethod(Method method) {
        Class<?> returnType = method.getReturnType();
        if (Void.class.equals(returnType) || void.class.equals(returnType)) {
            throw createInvalidSignatureException(method);
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 2) {
            throw createInvalidSignatureException(method);
        }

        if (!(Object.class.equals(parameterTypes[0]) || String.class.equals(parameterTypes[0]))) {
            throw createInvalidSignatureException(method);
        }

        if (!Type.class.equals(parameterTypes[1])) {
            throw createInvalidSignatureException(method);
        }

        return method;
    }

    private static InvalidMethodSignatureException createInvalidSignatureException(Method method) {
        return InvalidMethodSignatureException.builder(method)
                                              .addAnnotation(DefaultDataTableCellTransformer.class)
                                              .addSignature("public Object defaultDataTableCell(String fromValue, Type toValueType)")
                                              .addSignature("public Object defaultDataTableCell(Object fromValue, Type toValueType)")
                                              .build();
    }

    @Override
    public TableCellByTypeTransformer tableCellByTypeTransformer() {
        return transformer;
    }

}
