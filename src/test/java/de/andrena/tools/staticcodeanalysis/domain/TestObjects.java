package de.andrena.tools.staticcodeanalysis.domain;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

public class TestObjects {
    public static final String BASE_PACKAGE = "de";
    public static final ClassReference MY_CALLER = new ClassReference("de.MyCaller");
    public static final ClassReference MY_OTHER_CALLER = new ClassReference("de.MyOtherCaller");
    public static final ClassReference MY_SERVICE = new ClassReference("de.MyService");
    public static final MethodReference method = new MethodReference(MY_SERVICE, "method", "method","()");
    public static final MethodReference otherMethod = new MethodReference(MY_SERVICE, "otherMethod", "otherMethod","()");
    public static final ClassReference IRRELEVANT_CLASS = new ClassReference("com.Irrelevant");
}
