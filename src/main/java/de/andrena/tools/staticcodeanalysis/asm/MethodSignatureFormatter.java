package de.andrena.tools.staticcodeanalysis.asm;

import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.stream.Collectors;

public class MethodSignatureFormatter {


    public String format(String methodName, String signature) {
        var type = Type.getMethodType(signature);
        return getShortClassName(type.getReturnType()) +
                " " +
                methodName +
                "(" +
                Arrays.stream(type.getArgumentTypes()).map(this::getShortClassName).collect(Collectors.joining(",")) +
                ")";

    }

    private String getShortClassName(Type it) {
        return it.getClassName().replaceAll(".*\\.", "");
    }

}
