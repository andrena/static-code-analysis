package de.andrena.tools.staticcodeanalysis.classgraph;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassGraphClassFinderTest {

    @Test
    void findClasses() {
        var classes = new ClassGraphClassFinder().findAllRelevantClasses("de.andrena.tools.staticcodeanalysis.sample.typeMapping");
        assertThat(classes).containsExactlyInAnyOrder("de.andrena.tools.staticcodeanalysis.sample.typeMapping.Value",
                "de.andrena.tools.staticcodeanalysis.sample.typeMapping.Caller",
                "de.andrena.tools.staticcodeanalysis.sample.typeMapping.Service");
    }

}