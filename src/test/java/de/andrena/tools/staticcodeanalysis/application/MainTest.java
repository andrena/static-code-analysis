package de.andrena.tools.staticcodeanalysis.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

    public static final String NEWLINE = System.getProperty("line.separator");
    private final TestAppender appender = new TestAppender();

    @BeforeEach
    void setUp() {
        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).addAppender(appender);
    }

    @AfterEach
    void tearDown() {

        ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).removeAppender(appender);
    }

    @Test
    void noKeyword_ThrowsException() {
        Assertions.assertThatThrownBy(() -> Main.main(new String[0])).isInstanceOf(InvalidUsageException.class).hasMessage("required parameters: <selected analysis> <further parameters>");
    }

    @Test
    void invalidKeyword_ThrowsException() {
        Assertions.assertThatThrownBy(() -> Main.main(new String[]{"invalid"})).isInstanceOf(InvalidUsageException.class).hasMessage("unknown keyword: invalid");
    }

    @Test
    void invocations_WrongArguments_ThrowsException() {
        Assertions.assertThatThrownBy(() -> Main.main(new String[]{"invocations"})).isInstanceOf(InvalidUsageException.class).hasMessage("invocations: required parameters: <base package> <class name pattern>");
    }

    @Test
    void invocations_allAccessibleMethodsAreRegarded_ButOnlyIfInvoked() throws InvalidUsageException {
        Main.main(new String[]{"invocations", "de.andrena.tools.staticcodeanalysis.sample.invocations", ".*AccessibilityTestService"});
        var allLogs = getLogLines();

        assertEquals(expectedResultForOneInvokedClass(), allLogs);
    }

    @Test
    void invocationsAreCollected() throws InvalidUsageException {
        Main.main(new String[]{"invocations", "de.andrena.tools.staticcodeanalysis.sample.invocations", ".*Service"});
        var allLogs = getLogLines();

        var expected = expectedResultForManyInvokedClasses();
        assertEquals(expected, allLogs);
    }

    @Test
    void fieldAccesses_WrongArguments_ThrowsException() {
        Assertions.assertThatThrownBy(() -> Main.main(new String[]{"fieldAccesses"})).isInstanceOf(InvalidUsageException.class).hasMessage("fieldAccesses: required parameters: <base package> <class name pattern>");
    }

    @Test
    void fieldAccessesAreCollected() throws InvalidUsageException {
        Main.main(new String[]{"fieldAccesses", "de.andrena.tools.staticcodeanalysis.sample.fieldAccesses", ".*"});
        var allLogs = getLogLines();

        var expected = expectedResultForFieldAccesses();
        assertEquals(expected, allLogs);
    }

    @Test
    void fieldAccesses_PatternIsMatched() throws InvalidUsageException {
        Main.main(new String[]{"fieldAccesses", "de.andrena.tools.staticcodeanalysis.sample.fieldAccesses", ".*SimpleUsages"});
        var allLogs = getLogLines();

        assertThat(allLogs).contains("Found 1 classes");
    }

    private String expectedResultForFieldAccesses() {
        return """
                Analyzing classes with root package de.andrena.tools.staticcodeanalysis.sample.fieldAccesses, showing field accesses of classes matching pattern '.*'
                Found 5 classes
                Field accesses of public methods for de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.UsagesFromNonPublicMethods
                                
                  1 int usesA()
                                
                   1
                a  X
                                
                Field accesses of public methods for de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.TransitiveUsages
                                
                  1 long usesABTransitively()
                  2 long usesABTransitivelyViaPublicMethod()
                  3 long usesBTransitively()
                                
                   1  2  3
                a  X  X  \s
                b  X  X  X
                                
                Field accesses of public methods for de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.SimpleUsages
                                
                  1 int usesA()
                  2 long usesAB()
                  3 int usesC()
                                
                   1  2  3
                a  X  X  \s
                b     X  \s
                c        X
                                
                Field accesses of public methods for de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.UsagesFromLambdas
                                
                  1 int usesA()
                  2 long usesAB()
                                
                   1  2
                a  X  X
                b     X
                                
                Field accesses of public methods for de.andrena.tools.staticcodeanalysis.sample.fieldAccesses.UsagesInExpressions
                                
                  1 int usesA()
                  2 long usesAB()
                  3 int usesC()
                                
                   1  2  3
                a  X  X  \s
                b     X  \s
                c        X
                """;
    }

    private String expectedResultForOneInvokedClass() {
        return """
                Analyzing classes with root package de.andrena.tools.staticcodeanalysis.sample.invocations, showing invocations of classes matching pattern '.*AccessibilityTestService'
                Found 9 classes
                Found 1 invoked classes matching pattern .*AccessibilityTestService
                Dependencies for de.andrena.tools.staticcodeanalysis.sample.invocations.AccessibilityTestService

                  1 OneController

                                                         1
                                          void <init>()  X
                void packagePrivateMethodIsConsidered()  X
                     void protectedMethodIsConsidered()  X
                        void publicMethodIsConsidered()  X
                """;
    }

    private String expectedResultForManyInvokedClasses() {
        return """
                Analyzing classes with root package de.andrena.tools.staticcodeanalysis.sample.invocations, showing invocations of classes matching pattern '.*Service'
                Found 9 classes
                Found 4 invoked classes matching pattern .*Service
                Dependencies for de.andrena.tools.staticcodeanalysis.sample.invocations.AccessibilityTestService

                  1 OneController

                                                         1
                                          void <init>()  X
                void packagePrivateMethodIsConsidered()  X
                     void protectedMethodIsConsidered()  X
                        void publicMethodIsConsidered()  X

                Dependencies for de.andrena.tools.staticcodeanalysis.sample.invocations.InvocationTestService

                  1 OneController
                  2 OneController$1
                  3 OneController$InnerClass
                  4 OtherController
                  5 SuperController

                                                 1  2  3  4  5
                                  void <init>()  X        X  X
                void calledFromAnonymousClass()     X        \s
                    void calledFromInnerClass()        X     \s
                     void calledFromLambda(int)  X           \s
                    void calledFromSuperClass()              X
                                String map(int)  X        X  \s
                               String map(long)           X  \s

                Dependencies for de.andrena.tools.staticcodeanalysis.sample.invocations.PersonService

                  1 OneController

                               1
                void <init>()  X
                  void find()  X
                  void load()  X
                  void save()  X
                void update()  X

                Dependencies for de.andrena.tools.staticcodeanalysis.sample.invocations.SampleService

                  1 OneController
                  2 OtherController

                                            1  2
                             void <init>()  X  X
                String build(long,boolean)     X
                 String build(int,boolean)     X
                           String map(int)  X  \s
                          String map(long)  X  \s
                                """;
    }


    private String getLogLines() {
        return appender.getLoggedEvents().stream()
                .map(it -> it.getMessage().getFormattedMessage())
                .map(it -> it.replace(NEWLINE, "\n"))
                .filter(it -> !it.contains("Reflections took"))
                .collect(Collectors.joining("\n"));
    }
}