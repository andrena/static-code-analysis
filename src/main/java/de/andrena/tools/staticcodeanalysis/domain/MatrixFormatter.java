package de.andrena.tools.staticcodeanalysis.domain;

import de.andrena.tools.staticcodeanalysis.domain.model.ClassReference;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class MatrixFormatter<ROW, COLUMN> {
    public static final String NEWLINE = System.getProperty("line.separator");
    private final StringBuilder output = new StringBuilder();

    protected abstract String getCaption();

    protected abstract String getShortColumnName(COLUMN column);

    protected abstract String getShortRowName(ROW row);

    protected abstract Comparator<COLUMN> getColumnComparator();
    protected abstract Comparator<ROW> getRowComparator();

    public String formatMatrix(ClassReference classname, Map<ROW, ? extends Collection<COLUMN>> matrix) {
        if (matrix.isEmpty()) {
            return "";
        }
        output.append(getCaption()).append(" for ").append(classname.getFullName()).append(NEWLINE).append(NEWLINE);
        formatMatrix(matrix);
        return output.toString();
    }


    private void formatMatrix(Map<ROW, ? extends Collection<COLUMN>> matrix) {
        List<COLUMN> columns = matrix.values().stream().flatMap(Collection::stream).distinct().sorted(getColumnComparator()).toList();
        addHeaderWithCallers(columns);
        addMatrix(matrix, columns);
    }

    private void addHeaderWithCallers(List<COLUMN> columns) {
        for (int i = 0; i < columns.size(); i++) {
            output.append(formatIndex(i + 1))
                    .append(" ")
                    .append(getShortColumnName(columns.get(i)))
                    .append(NEWLINE);
        }
        output.append(NEWLINE);
    }

    private void addMatrix(Map<ROW, ? extends Collection<COLUMN>> matrix, List<COLUMN> callers) {
        var rows = matrix.keySet().stream().sorted(getRowComparator()).toList();
        var rowColumnWidth = rows.stream().map(this::getShortRowName).mapToInt(String::length).max().orElse(0);
        addMatrixHeader(callers.size(), rowColumnWidth);
        for (var method : rows) {
            addMatrixRow(matrix, callers, rowColumnWidth, method);
        }
    }

    private void addMatrixHeader(int count, int rowColumnWidth) {
        var columnHeaders = IntStream.rangeClosed(1, count)
                .mapToObj(this::formatIndex).collect(Collectors.joining());
        output.append(StringUtils.leftPad("", rowColumnWidth))
                .append(columnHeaders)
                .append(NEWLINE);
    }

    private void addMatrixRow(Map<ROW, ? extends Collection<COLUMN>> matrix, List<COLUMN> columns, int rowColumnsWidth, ROW row) {
        output.append(StringUtils.leftPad(getShortRowName(row), rowColumnsWidth));
        var actualRelatedColumns = matrix.get(row);
        for (var column : columns) {
            output.append(StringUtils.leftPad(actualRelatedColumns.contains(column) ? "X" : " ", 3));
        }
        output.append(NEWLINE);
    }

    private String formatIndex(int i) {
        return StringUtils.leftPad(String.valueOf(i), 3);
    }
}
