package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InvocationMatrixFormatter {
    public static final String NEWLINE = System.getProperty("line.separator");
    private final String basePackage;
    private final StringBuilder output = new StringBuilder();

    public InvocationMatrixFormatter(String basePackage) {
        this.basePackage = basePackage;
    }

    public String formatInvocationMatrix(Map<MethodReference, Collection<ClassReference>> invocations, ClassReference classname) {
        if (invocations.isEmpty()) {
            return "";
        }
        output.append("Dependencies for ").append(classname.getFullName()).append(NEWLINE).append(NEWLINE);
        formatInvocationMatrix(invocations);
        return output.toString();
    }

    private void formatInvocationMatrix(Map<MethodReference, Collection<ClassReference>> invocations) {
        List<ClassReference> callers = invocations.values().stream().flatMap(Collection::stream).distinct().sorted().toList();
        addHeaderWithCallers(callers);
        addMatrix(invocations, callers);
    }

    private void addHeaderWithCallers(List<ClassReference> callers) {
        for (int i = 0; i < callers.size(); i++) {
            output.append(formatIndex(i + 1))
                    .append(" ")
                    .append(callers.get(i).getShortName(basePackage))
                    .append(NEWLINE);
        }
        output.append(NEWLINE);
    }

    private void addMatrix(Map<MethodReference, Collection<ClassReference>> invocations, List<ClassReference> callers) {
        var calledMethods = invocations.keySet().stream().sorted(Comparator.comparing(MethodReference::name)).toList();
        var methodsColumnWidth = calledMethods.stream().map(MethodReference::method).mapToInt(String::length).max().orElse(0);
        addMatrixHeader(callers.size(), methodsColumnWidth);
        for (var method : calledMethods) {
            addMatrixRow(invocations, callers, methodsColumnWidth, method);
        }
    }

    private void addMatrixHeader(int count, int methodNamesLength) {
        var columnHeaders = IntStream.rangeClosed(1, count)
                .mapToObj(this::formatIndex).collect(Collectors.joining());
        output.append(StringUtils.leftPad("", methodNamesLength))
                .append(columnHeaders)
                .append(NEWLINE);
    }

    private void addMatrixRow(Map<MethodReference, Collection<ClassReference>> invocations, List<ClassReference> callers, int methodNamesLength, MethodReference method) {
        output.append(StringUtils.leftPad(method.method(), methodNamesLength));
        var actualCallers = invocations.get(method);
        for (var caller : callers) {
            output.append(StringUtils.leftPad(actualCallers.contains(caller) ? "X" : " ", 3));
        }
        output.append(NEWLINE);
    }

    private String formatIndex(int i) {
        return StringUtils.leftPad(String.valueOf(i), 3);
    }
}
