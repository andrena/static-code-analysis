package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;
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
        var result = new InvocationMatrixFormatter("").formatInvocationMatrix(Collections.emptyMap(), MY_CALLER);
        assertThat(result).isEmpty();
    }

    @Test
    void formatInvocationMatrix() {
        Map<MethodReference, Collection<ClassReference>> invocations = Map.of(
                method, Set.of(MY_CALLER, MY_OTHER_CALLER),
                otherMethod, Set.of(MY_CALLER, IRRELEVANT_CLASS)
        );

        var result = new InvocationMatrixFormatter(BASE_PACKAGE).formatInvocationMatrix(invocations, MY_SERVICE);
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