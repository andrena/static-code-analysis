package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.invocations.CallHandler;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ClassInvocationsVisitor extends ClassVisitor {
    private final CallHandler handler;
    private final String className;

    public ClassInvocationsVisitor(CallHandler handler, String className) {
        super(Opcodes.ASM9);
        this.handler = handler;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String desc, String sig, String[] ex) {
        return new MethodRefCollector(handler, className, name);
    }

}
