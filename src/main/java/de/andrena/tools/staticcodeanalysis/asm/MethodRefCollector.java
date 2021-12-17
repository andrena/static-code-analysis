package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.invocations.CallHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class MethodRefCollector extends MethodVisitor {
    private final CallHandler handler;
    private final ClassReference classReference;

    static final String MF_SIG = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
            + "Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/"
            + "MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";
    static final String ALT_SIG = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
            + "Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;";


    public MethodRefCollector(CallHandler handler, String className, String methodName) {
        super(Opcodes.ASM9);
        this.handler = handler;
        this.classReference = new ClassReference(className);
    }

    @Override
    public void visitMethodInsn(
            int opcode, String owner, String name, String desc, boolean itf) {
        var methodCall = new MethodSignatureFormatter().format(name, desc);
        handler.handleCall(classReference, new MethodReference(new ClassReference(Type.getObjectType(owner).getClassName()), methodCall, name, desc));
    }

    @Override
    public void visitInvokeDynamicInsn(
            String name, String desc, Handle bsm, Object... bsmArgs) {
        if (bsm.getOwner().equals("java/lang/invoke/LambdaMetafactory")
                && bsm.getDesc().equals(bsm.getName().equals("altMetafactory") ? ALT_SIG : MF_SIG)) {
            Handle target = (Handle) bsmArgs[1];
            visitMethodInsn(-1, target.getOwner(), target.getName(), target.getDesc(), false);
        }
    }
}
