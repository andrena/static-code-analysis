package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

import java.util.*;

public record MyFieldAccessHandler(Map<String, String> fieldsWithTypes, Map<String, Set<String>> accesses, List<String> invocations, Set<String> publicMethods)  implements FieldAccessHandler{
    public MyFieldAccessHandler() {
        this(new HashMap<>(), new HashMap<>(), new ArrayList<>(), new HashSet<>());
    }

    @Override
    public void handleFieldDefinition(String fieldName, String type) {
        fieldsWithTypes.put(fieldName, type);
    }


    @Override
    public void handlePublicMethodDefinition(MethodReference method) {
        publicMethods.add(method.name());
    }

    @Override
    public void handleAccess(MethodReference method, String fieldName) {
        accesses.computeIfAbsent(fieldName, irrelevant -> new HashSet<>()).add(method.name());
    }

    @Override
    public void handleInternalInvocation(MethodReference callingMethod, MethodReference invokedMethod) {
        invocations.add(callingMethod.name()+"->"+invokedMethod.name());
    }
}
