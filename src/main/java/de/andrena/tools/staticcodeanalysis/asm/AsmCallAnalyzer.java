package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.invocations.CallAnalyzer;
import de.andrena.tools.staticcodeanalysis.domain.invocations.CallHandler;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.Set;

public class AsmCallAnalyzer implements CallAnalyzer {

    @Override
    public void analyzeCalls(Set<String> classes, CallHandler handler) {
        for (String aClass : classes) {
            ClassReader reader = createReader(aClass);
            reader.accept(new ClassInvocationsVisitor(handler, aClass), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
    }

    private ClassReader createReader(String aClass) {
        try {
            return new ClassReader(aClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}