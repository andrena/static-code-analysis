package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static de.andrena.tools.staticcodeanalysis.domain.TestObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

class ClassInvocationsAnalyzerTest {

    private final ClassInvocationsAnalyzer analyzer = new ClassInvocationsAnalyzer(BASE_PACKAGE);

    @Test
    void selfCall_IsIgnored() {
        analyzer.handleInvocation(MY_CALLER, new MethodReference(MY_CALLER, "call", "call", "()"));
        assertThat(analyzer.getInvocationsOf(MY_CALLER)).isEmpty();
    }

    @Test
    void oneCall_IsTracked() {
        analyzer.handleInvocation(MY_CALLER, method);
        assertThat(analyzer.getInvocationsOf(MY_CALLER)).isEmpty();
        assertThat(analyzer.getInvocationsOf(MY_SERVICE)).containsExactly(entry(method, Set.of(MY_CALLER)));
    }

    @Test
    void oneCall_DuplicatesAreIgnored() {
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(MY_CALLER, method);
        assertThat(analyzer.getInvocationsOf(MY_CALLER)).isEmpty();
        assertThat(analyzer.getInvocationsOf(MY_SERVICE)).containsExactly(entry(method, Set.of(MY_CALLER)));
    }

    @Test
    void multipleCallsToOneClass_AreTracked_DuplicatesAreIgnored() {
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(MY_CALLER, otherMethod);
        analyzer.handleInvocation(MY_CALLER, otherMethod);
        analyzer.handleInvocation(MY_OTHER_CALLER, method);
        analyzer.handleInvocation(MY_OTHER_CALLER, method);

        assertThat(analyzer.getInvocationsOf(MY_CALLER)).isEmpty();
        assertThat(analyzer.getInvocationsOf(MY_OTHER_CALLER)).isEmpty();
        assertThat(analyzer.getInvocationsOf(MY_SERVICE)).containsExactlyInAnyOrderEntriesOf(
                Map.of(method, Set.of(MY_CALLER, MY_OTHER_CALLER),
                        otherMethod, Set.of(MY_CALLER))
        );
    }

    @Test
    void callsFromIrrelevantMethods_AreIgnored() {
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(IRRELEVANT_CLASS, method);

        assertThat(analyzer.getInvocationsOf(MY_SERVICE)).containsExactly(
                entry(method, Set.of(MY_CALLER))
        );
    }

    @Test
    void callsToIrrelevantMethods_AreIgnored() {
        analyzer.handleInvocation(MY_CALLER, new MethodReference(IRRELEVANT_CLASS, "irrelevant", "irrelevant", "()"));

        assertThat(analyzer.getInvocationsOf(IRRELEVANT_CLASS)).isEmpty();
    }

    @Test
    void circularCalls_AreTracked() {
        MethodReference callerMethod = new MethodReference(MY_CALLER, "callerMethod", "callerMethod", "()");
        MethodReference otherCallerMethod = new MethodReference(MY_OTHER_CALLER, "otherCallerMethod", "other", "()");
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(MY_CALLER, otherMethod);
        analyzer.handleInvocation(MY_OTHER_CALLER, callerMethod);
        analyzer.handleInvocation(MY_SERVICE, otherCallerMethod);

        assertThat(analyzer.getInvocationsOf(MY_CALLER)).containsExactly(
                entry(callerMethod, Set.of(MY_OTHER_CALLER)));
        assertThat(analyzer.getInvocationsOf(MY_OTHER_CALLER)).containsExactly(
                entry(otherCallerMethod, Set.of(MY_SERVICE)));
        assertThat(analyzer.getInvocationsOf(MY_SERVICE)).containsExactlyInAnyOrderEntriesOf(
                Map.of(method, Set.of(MY_CALLER),
                        otherMethod, Set.of(MY_CALLER))
        );
    }

    @Test
    void findInvokedClassesMatching_NoCalls_EmptyResult() {
        assertThat(analyzer.findInvokedClassesMatching(".*")).isEmpty();
    }

    @Test
    void findInvokedClassesMatching_OneCall() {
        analyzer.handleInvocation(MY_CALLER, method);
        assertThat(analyzer.findInvokedClassesMatching(".*Caller")).isEmpty();
        assertThat(analyzer.findInvokedClassesMatching(".*Service")).containsExactly(MY_SERVICE);
    }

    @Test
    void findInvokedClassesMatching_DuplicateCalls_ReturnsOneCall() {
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(MY_CALLER, method);
        assertThat(analyzer.findInvokedClassesMatching(".*Caller")).isEmpty();
        assertThat(analyzer.findInvokedClassesMatching(".*Service")).containsExactly(MY_SERVICE);
    }

    @Test
    void findInvokedClassesMatching_FullNameIsConsidered() {
        analyzer.handleInvocation(MY_CALLER, method);
        assertThat(analyzer.findInvokedClassesMatching(".*Service")).containsExactly(MY_SERVICE);
        assertThat(analyzer.findInvokedClassesMatching("MyService")).isEmpty();
    }

    @Test
    void findInvokedClassesMatching_MultipleCalls_ResultIsSorted() {
        analyzer.handleInvocation(MY_CALLER, method);
        analyzer.handleInvocation(MY_CALLER, otherMethod);
        analyzer.handleInvocation(MY_OTHER_CALLER, method);
        analyzer.handleInvocation(MY_OTHER_CALLER, new MethodReference(MY_CALLER, "caller", "caller", "()"));

        assertThat(analyzer.findInvokedClassesMatching(".*OtherCaller")).isEmpty();
        assertThat(analyzer.findInvokedClassesMatching(".*Caller")).containsExactly(MY_CALLER);
        assertThat(analyzer.findInvokedClassesMatching(".*Service")).containsExactly(MY_SERVICE);
        assertThat(analyzer.findInvokedClassesMatching(".*")).containsExactly(MY_CALLER, MY_SERVICE);
    }

}