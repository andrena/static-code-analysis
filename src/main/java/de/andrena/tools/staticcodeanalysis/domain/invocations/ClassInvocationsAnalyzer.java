package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ClassInvocationsAnalyzer implements CallHandler {

    private final Map<ClassReference, Set<MethodReference>> calls = new HashMap<>();
    private final Map<MethodReference, Set<ClassReference>> calledBy = new HashMap<>();
    private final String packagePrefix;

    public ClassInvocationsAnalyzer(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    @Override
    public void handleCall(ClassReference caller, MethodReference call) {
        if (caller.equals(call.classReference()) || !isRelevant(call) || !isRelevant(caller)) {
            return;
        }
        calls.computeIfAbsent(caller, it -> new HashSet<>()).add(call);
        calledBy.computeIfAbsent(call, it -> new HashSet<>()).add(caller);
    }

    private boolean isRelevant(MethodReference call) {
        return call.isInPackage(packagePrefix);
    }

    private boolean isRelevant(ClassReference caller) {
        return caller.isInPackage(packagePrefix);
    }

    public List<ClassReference> findInvokedClassesMatching(String namePattern) {
        return calledBy.keySet().stream().filter(it -> it.classNameMatches(namePattern))
                .map(MethodReference::classReference).distinct().sorted().toList();
    }

    public Map<MethodReference, Collection<ClassReference>> getInvocationsOf(ClassReference className) {
        return calledBy.entrySet().stream()
                .filter(it -> it.getKey().isFromClass(className))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
