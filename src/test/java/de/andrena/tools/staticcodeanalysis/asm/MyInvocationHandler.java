package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodInvocation;

import java.util.ArrayList;
import java.util.List;

public class MyInvocationHandler implements InvocationHandler {

	private final List<ClassReference> callers = new ArrayList<>();
	private final List<MethodInvocation> methods = new ArrayList<>();

	@Override
	public void handleInvocation(ClassReference caller, MethodInvocation invocation) {
		callers.add(caller);
		methods.add(invocation);
	}

	public List<ClassReference> getCallers() {
		return callers;
	}

	public List<MethodInvocation> getMethods() {
		return methods;
	}
	
	

}
