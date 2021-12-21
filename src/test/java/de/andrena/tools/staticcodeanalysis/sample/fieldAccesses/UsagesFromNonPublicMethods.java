package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses;

@SuppressWarnings("all")
public class UsagesFromNonPublicMethods {

	private int a;

	public int usesA() {
		return a;
	}

	protected int usesAButProtected() {
		return a;
	}

	int usesAButPackagePrivate() {
		return a;
	}

	private int usesAButPrivate() {
		return a;
	}

}
