package de.andrena.tools.staticcodeanalysis.domain.fields;

public interface FieldAccessProcessor {
    void analyzeFieldAccesses(String aClass, FieldAccessHandler handler);
}
