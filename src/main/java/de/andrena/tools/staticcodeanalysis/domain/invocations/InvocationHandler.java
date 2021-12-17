package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

public interface InvocationHandler {
    void handleInvocation(ClassReference caller, MethodReference invocation);
}
