package de.andrena.tools.staticcodeanalysis.sample.invocations;

public class AccessibilityTestService {
    public void methodsThatAreNotInvokedAreIgnored() {

    }

    public void publicMethodIsConsidered() {

    }

    protected void protectedMethodIsConsidered() {

    }

    void packagePrivateMethodIsConsidered() {

    }

    private void privateMethodIsIgnored() {

    }
}
