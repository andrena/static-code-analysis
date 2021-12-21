package de.andrena.tools.staticcodeanalysis.domain.fields;

import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

public interface FieldAccessHandler {
    void handleFieldDefinition(String fieldName, String type);
    void handlePublicMethodDefinition(MethodReference method);
    void handleAccess(MethodReference method, String fieldName);
    void handleInternalInvocation(MethodReference callingMethod, MethodReference invokedMethod);

}
