package com.lfl.utils.snippet;

import io.cucumber.java.PendingException;

import java.text.MessageFormat;

public final class JavaSnippet extends AbstractJavaSnippet {
    public JavaSnippet() {
    }

    @Override
    public MessageFormat template() {
        return new MessageFormat("" +
                                 "@{0}(\"{1}\")\n" +
                                 "public void {2}({3}) '{'\n" +
                                 "    // {4}\n" +
                                 "{5}    throw new " + PendingException.class.getName() + "();\n" +
                                 "'}'");
    }
}
