package de.andrena.tools.staticcodeanalysis.application;

public class InvalidUsageException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidUsageException(String message) {
		super(message);
	}
}
