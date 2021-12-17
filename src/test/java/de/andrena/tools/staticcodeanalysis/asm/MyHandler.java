package de.andrena.tools.staticcodeanalysis.asm;

import de.andrena.tools.staticcodeanalysis.domain.invocations.CallHandler;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

import java.util.ArrayList;
import java.util.List;

public class MyHandler implements CallHandler{

	private final List<ClassReference> callers = new ArrayList<>();
	private final List<MethodReference> methods = new ArrayList<>();

	@Override
	public void handleCall(ClassReference caller, MethodReference call) {
		callers.add(caller);
		methods.add(call);
	}

	public List<ClassReference> getCallers() {
		return callers;
	}

	public List<MethodReference> getMethods() {
		return methods;
	}
	
	

}
