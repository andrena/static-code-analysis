package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.asm.fieldAccesses.ClassFieldsAccessVisitor;
import de.andrena.tools.staticcodeanalysis.asm.invocations.ClassInvocationsVisitor;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessProcessor;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessHandler;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationProcessor;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationHandler;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.Set;

public class AsmProcessor implements InvocationProcessor, FieldAccessProcessor {

    @Override
    public void analyzeInvocations(Set<String> classes, InvocationHandler handler) {
        for (String aClass : classes) {
            ClassReader reader = createReader(aClass);
            reader.accept(new ClassInvocationsVisitor(handler, aClass), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
    }

    @Override
    public void analyzeFieldAccesses(String aClass, FieldAccessHandler handler) {
        ClassReader reader = createReader(aClass);
        reader.accept(new ClassFieldsAccessVisitor(handler, aClass), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }

    private ClassReader createReader(String aClass) {
        try {
            return new ClassReader(aClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}