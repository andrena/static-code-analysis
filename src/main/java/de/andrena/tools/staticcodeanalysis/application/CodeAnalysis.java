package de.andrena.tools.staticcodeanalysis.application;

public abstract class CodeAnalysis {

    public boolean accept(String command) {
        return getKeyword().equals(command);
    }

    protected abstract String getKeyword();

    public abstract void run(String[] args) throws InvalidUsageException;
}
