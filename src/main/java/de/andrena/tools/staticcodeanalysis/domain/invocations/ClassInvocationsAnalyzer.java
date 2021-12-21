package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodInvocation;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class ClassInvocationsAnalyzer implements InvocationHandler {

    private final Map<ClassReference, Set<MethodInvocation>> invocations = new HashMap<>();
    private final Map<MethodInvocation, Set<ClassReference>> invokedBy = new HashMap<>();
    private final String packagePrefix;

    public ClassInvocationsAnalyzer(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    @Override
    public void handleInvocation(ClassReference caller, MethodInvocation invocation) {
        if (caller.equals(invocation.classReference()) || !isRelevant(invocation) || !isRelevant(caller)) {
            return;
        }
        invocations.computeIfAbsent(caller, it -> new HashSet<>()).add(invocation);
        invokedBy.computeIfAbsent(invocation, it -> new HashSet<>()).add(caller);
    }

    private boolean isRelevant(MethodInvocation invocation) {
        return invocation.isInPackage(packagePrefix);
    }

    private boolean isRelevant(ClassReference caller) {
        return caller.isInPackage(packagePrefix);
    }

    public List<ClassReference> findInvokedClassesMatching(String namePattern) {
        return invokedBy.keySet().stream().filter(it -> it.classNameMatches(namePattern))
                .map(MethodInvocation::classReference).distinct().sorted(Comparator.comparing(ClassReference::className)).toList();
    }

    public Map<MethodInvocation, Collection<ClassReference>> getInvocationsOf(ClassReference className) {
        return invokedBy.entrySet().stream()
                .filter(it -> it.getKey().isFromClass(className))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
