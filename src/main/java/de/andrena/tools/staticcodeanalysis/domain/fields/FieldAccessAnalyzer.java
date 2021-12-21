package de.andrena.tools.staticcodeanalysis.domain.fields;

import com.google.common.graph.Graphs;
import com.google.common.graph.MutableGraph;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.graph.GraphBuilder.directed;

public class FieldAccessAnalyzer implements FieldAccessHandler {
    private final MutableGraph<MethodReference> callGraph = directed().allowsSelfLoops(true).build();
    private Map<String, String> fieldsWithTypes = new HashMap<>();
    private final Map<MethodReference, Set<String>> accesses = new HashMap<>();
    private Set<MethodReference> relevantMethods = new HashSet<>();

    @Override
    public void handleFieldDefinition(String fieldName, String type) {
        fieldsWithTypes.put(fieldName, type);
    }

    @Override
    public void handlePublicMethodDefinition(MethodReference method) {
        relevantMethods.add(method);
        callGraph.addNode(method);
        callGraph.putEdge(method, method);
    }

    @Override
    public void handleAccess(MethodReference method, String fieldName) {
        accesses.computeIfAbsent(method, key -> new HashSet<>()).add(fieldName);
    }

    @Override
    public void handleInternalInvocation(MethodReference callingMethod, MethodReference invokedMethod) {
        callGraph.addNode(callingMethod);
        callGraph.addNode(invokedMethod);
        callGraph.putEdge(callingMethod, invokedMethod);
    }

    public Map<String, Set<MethodReference>> determineFieldAccessMatrix() {
        var transitiveGraph = Graphs.transitiveClosure(callGraph);
        return relevantMethods.stream()
                .flatMap(caller -> collectTransitiveAccesses(caller, transitiveGraph.successors(caller)).stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toSet())));

    }

    private Set<Map.Entry<String, MethodReference>> collectTransitiveAccesses(MethodReference caller, Set<MethodReference> successors) {
        var transitiveAccesses = successors.stream().flatMap(callee -> accesses.getOrDefault(callee, Collections.emptySet()).stream()).collect(Collectors.toSet());
        return transitiveAccesses.stream().map(field -> new AbstractMap.SimpleEntry<>(field, caller)).collect(Collectors.toSet());
    }
}
