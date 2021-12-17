package de.andrena.tools.staticcodeanalysis.application;

import de.andrena.tools.staticcodeanalysis.asm.AsmCallAnalyzer;
import de.andrena.tools.staticcodeanalysis.domain.invocations.CallAnalyzer;
import de.andrena.tools.staticcodeanalysis.domain.invocations.ClassFinder;
import de.andrena.tools.staticcodeanalysis.domain.invocations.ClassInvocationsAnalyzer;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationMatrixFormatter;
import de.andrena.tools.staticcodeanalysis.classgraph.ClassGraphClassFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class Main {

    static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        ensureCorrectParameters(args);
        var basePackage = args[0];
        var namePattern = args[1];

        CallAnalyzer callAnalyzer = new AsmCallAnalyzer();
        ClassFinder finder = new ClassGraphClassFinder();
        ClassInvocationsAnalyzer analyzer = new ClassInvocationsAnalyzer(basePackage);

        logger.info("Analyzing classes with root package {}, showing calls to classes matching pattern '{}'", basePackage, namePattern);
        Set<String> classes = finder.findAllRelevantClasses(basePackage);
        logger.info("Found {} classes", classes.size());
        callAnalyzer.analyzeCalls(classes, analyzer);

        var matchingClasses = analyzer.findInvokedClassesMatching(namePattern);
        logger.info("Found {} invoked classes matching pattern {}", matchingClasses.size(), namePattern);

        for (var matchingClass : matchingClasses) {
            var invocations = analyzer.getInvocationsOf(matchingClass);
            var formattedOutput = new InvocationMatrixFormatter(basePackage).formatInvocationMatrix(invocations, matchingClass);
            if (formattedOutput.isEmpty()) {
                continue;
            }
            logger.info(formattedOutput);
        }
    }

    private static void ensureCorrectParameters(String[] args) {
        if (args.length != 2) {
            logger.info("Required Parameters: <base package> <class name pattern>");
            logger.info("");
            logger.info("All calls of the classes below the given package will be analyzed, and the invocation matrix will be shown for classes matching the given pattern.");
            logger.warn("Note: The classpath has to be specified.");
            System.exit(1);
        }
    }

}


