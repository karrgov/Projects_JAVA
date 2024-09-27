package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.*;

public class MarkdownTablePrinter implements TablePrinter {

    private static final int MIN_COLUMN_WIDTH = 3;
    private static final String COLUMN_SEPARATOR = "|";
    private static final String WHITESPACE = " ";
    private static final String COLUMN_HEADER_SEPARATOR = "-";

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        List<StringBuilder> rowsAtFirstEmpty = new ArrayList<>();
        for(int i = 0; i < table.getRowsCount() + 1; i++) {
            rowsAtFirstEmpty.add(new StringBuilder());
        }

        Map<String, Integer> maximumWidths = new HashMap<>();
        for(String columnName : table.getColumnNames()) {
            maximumWidths.put(columnName, Math.max(getColumnMaxWidth(columnName, table), MIN_COLUMN_WIDTH));
        }

        int rowCounter = 0;
        int alignmentCounter = 0;

        for(String columnName : table.getColumnNames()) {
            int maxWidthForCurrColumn = maximumWidths.get(columnName);
            StringBuilder currRow = rowsAtFirstEmpty.get(rowCounter);

            populateRow(currRow, columnName, maxWidthForCurrColumn);
            rowCounter++;
            currRow = rowsAtFirstEmpty.get(rowCounter);

            ColumnAlignment alignment = null;

            if(alignments.length > alignmentCounter) {
                alignment = alignments[alignmentCounter];
                alignmentCounter++;
            }

            int alignmentCharsCount = 0;
            if(alignment != null) {
                alignmentCharsCount = alignment.getAlignmentCharactersCount();
            }

            String columnThatSeparatesHeaders = COLUMN_HEADER_SEPARATOR.repeat(maxWidthForCurrColumn - alignmentCharsCount);
            columnThatSeparatesHeaders = placeColumnAlignment(columnThatSeparatesHeaders, alignment);
            populateRow(currRow, columnThatSeparatesHeaders, maxWidthForCurrColumn);

            rowCounter++;

            for(String data : table.getColumnData(columnName)) {
                currRow = rowsAtFirstEmpty.get(rowCounter);
                populateRow(currRow, data, maxWidthForCurrColumn);
                rowCounter++;
            }

            rowCounter = 0;
        }

        for(StringBuilder row : rowsAtFirstEmpty) {
            row.append(COLUMN_SEPARATOR);
        }

        return convertStringBuilderListToStringList(rowsAtFirstEmpty);
    }

    private int getColumnMaxWidth(String column, Table table) {
        int max = column.length();

        for(String value : table.getColumnData(column)) {
            if(value.length() > max) {
                max = value.length();
            }
        }

        return max;
    }

    private void populateRow(StringBuilder row, String data, int maxWidth) {
        int timesToRepeat = Math.max(maxWidth - data.length() + 1, 0);
        row.append(COLUMN_SEPARATOR).append(WHITESPACE).append(data).append(WHITESPACE.repeat(timesToRepeat));
    }

    private String placeColumnAlignment(String str, ColumnAlignment alignment) {
        return switch (alignment) {
            case LEFT -> ":" + str + " ";
            case RIGHT -> str + ": ";
            case CENTER -> ":" + str + ": ";
            case NOALIGNMENT -> " " + str + " ";
            case null -> str;
        };
    }

    private List<String> convertStringBuilderListToStringList(List<StringBuilder> list) {
        List<String> result = new LinkedList<>();

        for(StringBuilder builder : list) {
            result.add(builder.toString());
        }

        return result;
    }

}
