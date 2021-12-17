package de.andrena.tools.staticcodeanalysis.domain.invocations;

import java.util.Set;

public interface CallAnalyzer {
    void analyzeCalls(Set<String> classes, CallHandler handler);
}
