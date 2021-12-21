package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodInvocation;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static de.andrena.tools.staticcodeanalysis.domain.TestObjects.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InvocationMatrixFormatterTest {

    @Test
    void formatInvocationMatrix_EmptyInvocations_ReturnsEmptyString() {
        var result = new InvocationMatrixFormatter("").formatMatrix(MY_CALLER, Collections.emptyMap());
        assertThat(result).isEmpty();
    }

    @Test
    void formatInvocationMatrix() {
        Map<MethodInvocation, Collection<ClassReference>> invocations = Map.of(
                methodInvocation, Set.of(MY_CALLER, MY_OTHER_CALLER),
                otherMethodInvocation, Set.of(MY_CALLER, IRRELEVANT_CLASS)
        );

        var result = new InvocationMatrixFormatter(BASE_PACKAGE).formatMatrix(MY_SERVICE, invocations);
        var expected = """
                Dependencies for de.MyService
                                
                  1 com.Irrelevant
                  2 MyCaller
                  3 MyOtherCaller
                                
                             1  2  3
                     method     X  X
                otherMethod  X  X  \s
                """;
        assertEquals(expected, result.replace(InvocationMatrixFormatter.NEWLINE, "\n"));
    }

}