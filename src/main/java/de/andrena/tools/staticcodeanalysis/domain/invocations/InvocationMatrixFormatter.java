package de.andrena.tools.staticcodeanalysis.domain.invocations;

import de.andrena.tools.staticcodeanalysis.domain.MatrixFormatter;
import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodInvocation;

import java.util.Comparator;

public class InvocationMatrixFormatter extends MatrixFormatter<MethodInvocation, ClassReference> {
    private final String basePackage;

    public InvocationMatrixFormatter(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    protected String getCaption() {
        return "Dependencies";
    }

    @Override
    protected String getShortColumnName(ClassReference classReference) {
        return classReference.getShortName(basePackage);
    }

    @Override
    protected String getShortRowName(MethodInvocation methodReference) {
        return methodReference.method();
    }

    @Override
    protected Comparator<ClassReference> getColumnComparator() {
        return Comparator.comparing(ClassReference::className);
    }

    @Override
    protected Comparator<MethodInvocation> getRowComparator() {
        return Comparator.comparing(MethodInvocation::name);
    }

}
