package de.andrena.tools.staticcodeanalysis.domain.fields;

import de.andrena.tools.staticcodeanalysis.domain.TestObjects;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationMatrixFormatter;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FieldAccessMatrixFormatterTest {

    @Test
    void formatMatrix() {
        var result = new FieldAccessMatrixFormatter().formatMatrix(TestObjects.MY_SERVICE, Map.of(
                "a", Set.of(TestObjects.method, TestObjects.otherMethod),
                "b", Set.of(TestObjects.otherMethod)
        ));

        assertEquals("""
                Field accesses of public methods for de.MyService
                                        
                  1 void method()
                  2 void otherMethod()
                                        
                   1  2
                a  X  X
                b     X
                """, result.replace(InvocationMatrixFormatter.NEWLINE, "\n"));
    }

}