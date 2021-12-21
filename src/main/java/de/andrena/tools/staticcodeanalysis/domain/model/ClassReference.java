package de.andrena.tools.staticcodeanalysis.domain.model;

public record ClassReference(String className){

	public boolean classNameMatches(String namePattern) {
		return className.matches(namePattern);
	}

	public String getShortName(String basePackage) {
		var basePackageWithDot = addTrailingDotIfRequired(basePackage);
		if (className.startsWith(basePackageWithDot)) {
			return className.substring(basePackageWithDot.length());
		}
		return className;
	}

	private String addTrailingDotIfRequired(String basePackage) {
		if (basePackage.endsWith(".")) {
			return basePackage;
		}
		return basePackage + ".";
	}

	public String getFullName() {
		return className;
	}

	public boolean isInPackage(String packagePrefix) {
		return className.startsWith(addTrailingDotIfRequired(packagePrefix));
	}
}
