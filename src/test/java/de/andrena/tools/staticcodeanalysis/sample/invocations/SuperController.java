package de.andrena.tools.staticcodeanalysis.sample.invocations;

public class SuperController {

    public void callMapper() {
        new InvocationTestService().calledFromSuperClass();
    }
}
