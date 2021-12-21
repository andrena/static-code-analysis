package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodInvocation;
import de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.*;
import de.andrena.tools.staticcodeanalysis.sample.typeMapping.Caller;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AsmAnalyzerTest {

    private final MyFieldAccessHandler handler = new MyFieldAccessHandler();

    @Test
    void analyzeCalls() {
        MyInvocationHandler handler = new MyInvocationHandler();
        new AsmProcessor().analyzeInvocations(Set.of(Caller.class.getName()), handler);
        assertThat(handler.getMethods()).extracting(this::describe).containsExactlyInAnyOrder(
                "Object: void <init>()",
                "Service: void <init>()",
                "Value: void <init>()",
                "Service: void simple()",
                "Service: void simple(int)",
                "Service: int simpleReturn()",
                "Service: boolean checkValue(Value)",
                "Service: boolean checkValue(Value,String)",
                "ArrayList: void <init>()",
                "Service: boolean checkValue(Value,List,int)",
                "Service: List mapValues(List,boolean,byte,char,int,long,double,float,short,String[],Boolean)",
                "Service: long mapValues(boolean,String[])",
                "Service: Value[] mapValues(Value[])");
        assertThat(handler.getCallers()).extracting(ClassReference::getFullName).hasSameSizeAs(handler.getMethods())
                .allMatch(Caller.class.getName()::equals);
    }

    @Test
    void fieldAccesses() {
        new AsmProcessor()
                .analyzeFieldAccesses(SimpleUsages.class.getName(), handler);
        assertThat(handler.fieldsWithTypes()).containsExactlyInAnyOrderEntriesOf(
                Map.of("a", "int",
                        "b", "long",
                        "c", "short"));
        assertThat(handler.accesses()).containsExactlyInAnyOrderEntriesOf(
                Map.of("a", Set.of("usesA", "usesAB"),
                        "b", Set.of("usesAB"),
                        "c", Set.of("usesC"))
        );
    }

    @Test
    void fieldAccessesForExpressions() {
        new AsmProcessor()
                .analyzeFieldAccesses(SimpleUsages.class.getName(), handler);
        assertThat(handler.accesses()).containsExactlyInAnyOrderEntriesOf(
                Map.of("a", Set.of("usesA", "usesAB"),
                        "b", Set.of("usesAB"),
                        "c", Set.of("usesC"))
        );
    }

    @Test
    void fieldAccessesFromLambdas() {
        new AsmProcessor()
                .analyzeFieldAccesses(UsagesFromLambdas.class.getName(), handler);
        assertThat(handler.accesses()).containsExactlyInAnyOrderEntriesOf(
                Map.of("a", Set.of("usesAB", "lambda$usesA$1"),
                        "b", Set.of("lambda$usesAB$0"))
        );
    }

    @Test
    void fieldAccessesFromNonPublicMethods() {
        new AsmProcessor()
                .analyzeFieldAccesses(UsagesFromNonPublicMethods.class.getName(), handler);
        assertThat(handler.accesses()).containsExactlyInAnyOrderEntriesOf(
                Map.of("a", Set.of("usesA", "usesAButProtected", "usesAButPackagePrivate", "usesAButPrivate")));
        assertThat(handler.publicMethods()).containsExactly("usesA");
    }

    @Test
    void methodInvocationsAreCollected() {
        new AsmProcessor()
                .analyzeFieldAccesses(TransitiveUsages.class.getName(), handler);
        assertThat(handler.invocations()).containsExactlyInAnyOrder(
                "usesABTransitively->usesB",
                "usesABTransitively->usesAB",
                "usesABTransitivelyViaPublicMethod->usesABTransitively",
                "usesBTransitively->usesBIndirectly",
                "usesBIndirectly->usesB"
        );
    }

    private String describe(MethodInvocation it) {
        return it.classReference().className().replaceAll(".*\\.", "") + ": " + it.method();
    }
}
