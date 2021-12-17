package de.andrena.tools.staticcodeanalysis.domain.invocations;

import java.util.Set;

public interface ClassFinder {
    Set<String> findAllRelevantClasses(String basePackage);
}
