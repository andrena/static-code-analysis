package de.andrena.tools.staticcodeanalysis.domain.fields;

import de.andrena.tools.staticcodeanalysis.domain.MatrixFormatter;
import de.andrena.tools.staticcodeanalysis.domain.model.MethodReference;

import java.util.Comparator;
import java.util.function.Function;

public class FieldAccessMatrixFormatter extends MatrixFormatter<String, MethodReference> {

    @Override
    protected String getCaption() {
        return "Field accesses of public methods";
    }

    @Override
    protected String getShortColumnName(MethodReference method) {
        return method.method();
    }

    @Override
    protected String getShortRowName(String field) {
        return field;
    }

    @Override
    protected Comparator<MethodReference> getColumnComparator() {
        return Comparator.comparing(MethodReference::name);
    }

    @Override
    protected Comparator<String> getRowComparator() {
        return Comparator.comparing(Function.identity());
    }

}
