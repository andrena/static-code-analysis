package de.andrena.tools.staticcodeanalysis.asm;

import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TypeFormatter {


    public String formatMethodSignature(String methodName, String signature) {
        var type = Type.getMethodType(signature);
        return getShortClassName(type.getReturnType()) +
                " " +
                methodName +
                "(" +
                Arrays.stream(type.getArgumentTypes()).map(this::getShortClassName).collect(Collectors.joining(",")) +
                ")";

    }
    public String formatType(String typeDescriptor) {
        var type = Type.getType(typeDescriptor);
        return getShortClassName(type);
    }

    private String getShortClassName(Type it) {
        return it.getClassName().replaceAll(".*\\.", "");
    }

}
