package de.andrena.tools.staticcodeanalysis.asm.fieldAccesses;

import de.andrena.tools.staticcodeanalysis.asm.AsmConstants;
import de.andrena.tools.staticcodeanalysis.asm.TypeFormatter;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassFieldsAccessVisitor extends ClassVisitor {
    private final FieldAccessHandler handler;
    private final String className;
    private final TypeFormatter formatter = new TypeFormatter();

    public ClassFieldsAccessVisitor(FieldAccessHandler handler, String className) {
        super(AsmConstants.ASM_OPCODE);
        this.handler = handler;
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(
            int access, String name, String desc, String sig, String[] ex) {
        MethodReference method = new MethodReference(formatter.formatMethodSignature(name, desc), name, desc);
        if (access == Opcodes.ACC_PUBLIC && !AsmConstants.CONSTRUCTOR_NAME.equals(name)) {
            handler.handlePublicMethodDefinition(method);
        }
        return new FieldAccessCollector(handler, className, method);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        handler.handleFieldDefinition(name, formatter.formatType(descriptor));
        return super.visitField(access, name, descriptor, signature, value);
    }
}
