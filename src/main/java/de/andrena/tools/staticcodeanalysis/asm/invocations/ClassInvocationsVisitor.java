package de.andrena.tools.staticcodeanalysis.asm.invocations;

import de.andrena.tools.staticcodeanalysis.asm.AsmConstants;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class ClassInvocationsVisitor extends ClassVisitor {
    private final InvocationHandler handler;
    private final String className;

    public ClassInvocationsVisitor(InvocationHandler handler, String className) {
        super(AsmConstants.ASM_OPCODE);
        this.handler = handler;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String desc, String sig, String[] ex) {
        return new MethodInvocationCollector(handler, className);
    }

}
