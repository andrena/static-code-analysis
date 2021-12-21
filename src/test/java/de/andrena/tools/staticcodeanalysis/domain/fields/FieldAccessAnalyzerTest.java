package de.andrena.tools.staticcodeanalysis.domain.fields;

import de.andrena.tools.staticcodeanalysis.domain.TestObjects;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FieldAccessAnalyzerTest {

    @Test
    void determineFieldAccessMatrix() {
        FieldAccessAnalyzer analyzer = new FieldAccessAnalyzer();
        analyzer.handleFieldDefinition("a", "int");
        analyzer.handleFieldDefinition("b", "int");
        analyzer.handlePublicMethodDefinition(TestObjects.method);
        analyzer.handlePublicMethodDefinition(TestObjects.otherMethod);
        var privateMethod = new MethodReference("inner", "", "");

        analyzer.handleInternalInvocation(TestObjects.method, privateMethod);

        analyzer.handleAccess(TestObjects.otherMethod, "a");
        analyzer.handleAccess(TestObjects.otherMethod, "b");
        analyzer.handleAccess(privateMethod, "a");

        var result = analyzer.determineFieldAccessMatrix();

        assertThat(describe(result)).containsExactlyInAnyOrder(
                "a<-void method(),void otherMethod()",
                "b<-void otherMethod()");
    }

    @Test
    void determineFieldAccessMatrix_CanHandleRecursion() {
        FieldAccessAnalyzer analyzer = new FieldAccessAnalyzer();
        analyzer.handleFieldDefinition("a", "int");
        analyzer.handlePublicMethodDefinition(TestObjects.method);
        analyzer.handlePublicMethodDefinition(TestObjects.otherMethod);

        analyzer.handleInternalInvocation(TestObjects.method, TestObjects.otherMethod);
        analyzer.handleInternalInvocation(TestObjects.otherMethod, TestObjects.method);

        analyzer.handleAccess(TestObjects.otherMethod, "a");

        var result = analyzer.determineFieldAccessMatrix();

        assertThat(describe(result)).containsExactlyInAnyOrder(
                "a<-void method(),void otherMethod()");
    }

    private List<String> describe(Map<String, Set<MethodReference>> result) {
        return result.entrySet().stream().map(it -> it.getKey() + "<-" + it.getValue().stream().map(MethodReference::method).collect(Collectors.joining(","))).toList();
    }

}