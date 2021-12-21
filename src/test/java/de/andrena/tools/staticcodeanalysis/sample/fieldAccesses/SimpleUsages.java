package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses;

public class SimpleUsages {

    private int a;
    private long b;
    private short c;

    public long usesAB() {
        return a+b;
    }

    public int usesA() {
        return a;
    }

    public int usesC() {
        return c;
    }
}
