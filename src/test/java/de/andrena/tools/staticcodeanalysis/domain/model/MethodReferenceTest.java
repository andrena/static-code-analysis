package de.andrena.tools.staticcodeanalysis.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MethodReferenceTest {

    private static final String CLASS_NAME = "de.package.Class";
    private final ClassReference myClass = new ClassReference(CLASS_NAME);
    private final MethodReference myMethod = new MethodReference(myClass, "List call(String,List)", "java.util.List call(java.lang.String,java.util.List)","()");

    @Test
    void isInPackage() {
        assertThat(myMethod.isInPackage("de")).isTrue();
        assertThat(myMethod.isInPackage("de.")).isTrue();
        assertThat(myMethod.isInPackage("com")).isFalse();
        assertThat(myMethod.isInPackage("package")).isFalse();
    }

    @Test
    void isFromClass() {
        assertThat(myMethod.isFromClass(myClass)).isTrue();
        assertThat(myMethod.isFromClass(new ClassReference(CLASS_NAME))).isTrue();
        assertThat(myMethod.isFromClass(new ClassReference("package.Class"))).isFalse();
    }
}
