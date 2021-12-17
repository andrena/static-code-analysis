package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

import java.util.ArrayList;
import java.util.List;

public class MyHandler implements InvocationHandler {

	private final List<ClassReference> callers = new ArrayList<>();
	private final List<MethodReference> methods = new ArrayList<>();

	@Override
	public void handleInvocation(ClassReference caller, MethodReference invocation) {
		callers.add(caller);
		methods.add(invocation);
	}

	public List<ClassReference> getCallers() {
		return callers;
	}

	public List<MethodReference> getMethods() {
		return methods;
	}
	
	

}
