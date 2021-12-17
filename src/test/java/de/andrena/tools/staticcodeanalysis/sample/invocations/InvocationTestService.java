package de.andrena.tools.staticcodeanalysis.sample.invocations;

public class InvocationTestService {
    public String map(int value) {
        return String.valueOf(value);
    }
    public String map(long value) {
        return String.valueOf(value);
    }
    public String map(short value) {
        return String.valueOf(value);
    }

    public void calledFromLambda(int i) {
    }

    public void calledFromInnerClass() {
    }

    public void calledFromSuperClass() {
    }

    public void calledFromAnonymousClass() {
    }
}
