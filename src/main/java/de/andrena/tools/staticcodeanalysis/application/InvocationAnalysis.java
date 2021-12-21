package de.andrena.tools.staticcodeanalysis.application;

import de.andrena.tools.staticcodeanalysis.asm.AsmProcessor;
import de.andrena.tools.staticcodeanalysis.classgraph.ClassGraphClassFinder;
import de.andrena.tools.staticcodeanalysis.domain.invocations.ClassFinder;
import de.andrena.tools.staticcodeanalysis.domain.invocations.ClassInvocationsAnalyzer;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationMatrixFormatter;
import de.andrena.tools.staticcodeanalysis.domain.invocations.InvocationProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class InvocationAnalysis extends CodeAnalysis {
    static Logger logger = LogManager.getLogger(InvocationAnalysis.class);

    @Override
    public String getKeyword() {
        return "invocations";
    }

    @Override
    public void run(String[] args) throws InvalidUsageException {
        ensureCorrectParameters(args);
        var basePackage = args[0];
        var classNamePattern = args[1];

        InvocationProcessor invocationProcessor = new AsmProcessor();
        ClassFinder finder = new ClassGraphClassFinder();
        ClassInvocationsAnalyzer analyzer = new ClassInvocationsAnalyzer(basePackage);

        logger.info("Analyzing classes with root package {}, showing invocations of classes matching pattern '{}'", basePackage, classNamePattern);
        Set<String> classes = finder.findAllRelevantClasses(basePackage);
        logger.info("Found {} classes", classes.size());
        invocationProcessor.analyzeInvocations(classes, analyzer);

        var matchingClasses = analyzer.findInvokedClassesMatching(classNamePattern);
        logger.info("Found {} invoked classes matching pattern {}", matchingClasses.size(), classNamePattern);

        for (var matchingClass : matchingClasses) {
            var invocations = analyzer.getInvocationsOf(matchingClass);
            var formattedOutput = new InvocationMatrixFormatter(basePackage).formatMatrix(matchingClass, invocations);
            if (formattedOutput.isEmpty()) {
                continue;
            }
            logger.info(formattedOutput);
        }
    }

    private void ensureCorrectParameters(String[] args) throws InvalidUsageException {
        if (args.length != 2) {
            logger.info("Required parameters: <base package> <class name pattern>");
            logger.info("");
            logger.info("All calls of the classes below the given package will be analyzed, and the invocation matrix will be shown for classes matching the given pattern.");
            logger.warn("Note: The classpath has to be specified.");
            throw new InvalidUsageException(getKeyword()+": required parameters: <base package> <class name pattern>");
        }
    }
}
