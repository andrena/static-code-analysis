package de.andrena.tools.staticcodeanalysis.classgraph;

import de.andrena.tools.staticcodeanalysis.domain.invocations.ClassFinder;
import io.github.classgraph.ClassGraph;

import java.util.Set;
import java.util.stream.Collectors;

public class ClassGraphClassFinder implements ClassFinder {

    @Override
    public Set<String> findAllRelevantClasses(String basePackage) {
        var result = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(basePackage)
                .scan();
        var classes = result.getAllClasses().stream().map(it -> it.loadClass().getName()).collect(Collectors.toSet());
        var innerClasses = result.getAllClasses().stream().flatMap(it -> it.getInnerClasses().stream()).map(it -> it.loadClass().getName()).collect(Collectors.toSet());
        classes.addAll(innerClasses);
        return classes;
    }
}
