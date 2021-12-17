package de.andrena.tools.staticcodeanalysis.application;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void allAccessibleMethodsAreRegarded_ButOnlyIfInvoked() {
        Main.main(new String[]{"de.andrena.tools.staticcodeanalysis.sample.invocations", ".*AccessibilityTestService"});
        var allLogs = getLogLines();

        assertEquals(expectedResultForOneInvokedClass(), allLogs);
    }

    @Test
    void invocationsAreCollected() {
        Main.main(new String[]{"de.andrena.tools.staticcodeanalysis.sample.invocations", ".*Service"});
        var allLogs = getLogLines();

        var expected = expectedResultForManyInvokedClasses();
        assertEquals(expected, allLogs);
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