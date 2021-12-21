package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodInvocation;

public interface InvocationHandler {
    void handleInvocation(ClassReference caller, MethodInvocation invocation);
}
