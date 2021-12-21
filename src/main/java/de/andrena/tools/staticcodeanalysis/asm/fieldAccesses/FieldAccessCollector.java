package de.andrena.tools.staticcodeanalysis.asm.fieldAccesses;

import de.andrena.tools.staticcodeanalysis.asm.AsmConstants;
import de.andrena.tools.staticcodeanalysis.asm.TypeFormatter;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

class FieldAccessCollector extends MethodVisitor {
    private final FieldAccessHandler handler;
    private final ClassReference classReference;
    private final TypeFormatter formatter = new TypeFormatter();
    private MethodReference method;

    static final String MF_SIG = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
            + "Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/"
            + "MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;";
    static final String ALT_SIG = "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;"
            + "Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;";


    public FieldAccessCollector(FieldAccessHandler handler, String className, MethodReference method) {
        super(AsmConstants.ASM_OPCODE);
        this.handler = handler;
        this.classReference = new ClassReference(className);
        this.method = method;
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        handler.handleAccess(method, name);
    }

    @Override
    public void visitMethodInsn(
            int opcode, String owner, String name, String desc, boolean itf) {
        var invokedClass = new ClassReference(Type.getObjectType(owner).getClassName());
        if (classReference.equals(invokedClass)) {
            handler.handleInternalInvocation(method, new MethodReference(formatter.formatMethodSignature(name, desc), name, desc));
        }
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
