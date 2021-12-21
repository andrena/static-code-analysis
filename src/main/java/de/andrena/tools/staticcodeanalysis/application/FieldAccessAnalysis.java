package de.andrena.tools.staticcodeanalysis.application;

import de.andrena.tools.staticcodeanalysis.asm.AsmProcessor;
import de.andrena.tools.staticcodeanalysis.classgraph.ClassGraphClassFinder;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessAnalyzer;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessMatrixFormatter;
import de.andrena.tools.staticcodeanalysis.domain.fields.FieldAccessProcessor;
import de.andrena.tools.staticcodeanalysis.domain.invocations.ClassFinder;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;
import java.util.stream.Collectors;

public class FieldAccessAnalysis extends CodeAnalysis {
    static Logger logger = LogManager.getLogger(FieldAccessAnalysis.class);

    @Override
    public String getKeyword() {
        return "fieldAccesses";
    }

    @Override
    public void run(String[] args) throws InvalidUsageException {
        ensureCorrectParameters(args);
        var basePackage = args[0];
        var classNamePattern = args[1];

        FieldAccessProcessor processor = new AsmProcessor();
        ClassFinder finder = new ClassGraphClassFinder();

        logger.info("Analyzing classes with root package {}, showing field accesses of classes matching pattern '{}'", basePackage, classNamePattern);
        Set<String> classes = finder.findAllRelevantClasses(basePackage).stream().filter(it -> it.matches(classNamePattern)).collect(Collectors.toSet());
        logger.info("Found {} classes", classes.size());

        for (String aClass : classes) {
            FieldAccessAnalyzer accessAnalyzer = new FieldAccessAnalyzer();
            processor.analyzeFieldAccesses(aClass, accessAnalyzer);
            var formattedOutput = new FieldAccessMatrixFormatter().formatMatrix(new ClassReference(aClass),accessAnalyzer.determineFieldAccessMatrix());
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
            logger.info("For each class below the given package that matches the given name, the accesses to fields will be analyzed, and the field access matrix will be shown.");
            logger.warn("Note: The classpath has to be specified.");
            throw new InvalidUsageException(getKeyword()+": required parameters: <base package> <class name pattern>");
        }
    }
}
