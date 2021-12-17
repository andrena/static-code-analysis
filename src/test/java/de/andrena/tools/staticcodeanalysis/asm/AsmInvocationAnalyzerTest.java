package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import de.andrena.tools.staticcodeanalysis.sample.typeMapping.Caller;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AsmInvocationAnalyzerTest {

    @Test
    void analyzeCalls() {
        MyHandler handler = new MyHandler();
        new AsmInvocationAnalyzer().analyzeInvocations(Set.of(Caller.class.getName()), handler);
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

    private String describe(MethodReference it) {
        return it.classReference().className().replaceAll(".*\\.","")+": "+it.method();
    }
}
