package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses;

public class TransitiveUsages {

    private int a;
    private long b;

    public long usesABTransitively() {
        return usesB() + usesAB();
    }

    public long usesABTransitivelyViaPublicMethod() {
        return usesABTransitively();
    }

    public long usesBTransitively() {
        return usesBIndirectly();
    }

    private long usesBIndirectly() {
        return usesB();
    }

    private long usesB() {
        return b;
    }

    private long usesAB() {
        return a + b;
    }
}
