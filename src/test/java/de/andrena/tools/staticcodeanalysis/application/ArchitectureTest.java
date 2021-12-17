package de.andrena.tools.staticcodeanalysis.application;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackage;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTag;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;

@ArchTag("ArchitectureTest")
@AnalyzeClasses(packages = {"de.andrena.tools"}, importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureTest {

	    @ArchTest
	    public static final ArchRule architecture = Architectures.onionArchitecture()
        .domainModels("..model..")
        .domainServices("..domain..")
        .applicationServices("..application..")
        .adapter("asm", "..asm..")
        .adapter("classgraph", "..classgraph..")
        .ignoreDependency(resideInAPackage("..application.."), alwaysTrue())
        .ignoreDependency(alwaysTrue(), resideOutsideOfPackage("de.andrena.."));
}
