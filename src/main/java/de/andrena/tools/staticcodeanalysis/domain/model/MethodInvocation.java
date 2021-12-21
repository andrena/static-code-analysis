package de.andrena.tools.staticcodeanalysis.domain.model;

public record MethodInvocation(ClassReference classReference, String method, String name, String rawSignature){

	public boolean isFromClass(ClassReference otherReference) {
		return classReference.equals(otherReference);
	}

	public boolean isInPackage(String packagePrefix) {
		return classReference.isInPackage(packagePrefix);
	}

    public boolean classNameMatches(String namePattern) {
		return classReference.classNameMatches(namePattern);
    }
}
