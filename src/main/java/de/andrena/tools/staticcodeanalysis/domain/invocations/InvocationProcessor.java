package de.andrena.tools.staticcodeanalysis.domain.invocations;

import java.util.Set;

public interface InvocationProcessor {
    void analyzeInvocations(Set<String> classes, InvocationHandler handler);
}
