package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses;

public class UsagesInExpressions {

    private int a;
    private long b;
    private short c;

    public long usesAB() {
        if (a == b) {
            return a;
        }
        return 0;
    }

    public int usesA() {
        var result = 0;
        for (int i = 0; i < a; i++) {
            result += i;
        }
        return result;
    }

    public int usesC() {
        return c > 0 ? 1 : 0;
    }
}
