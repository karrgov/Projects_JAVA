package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class MarkdownTablePrinterTest {

    private TablePrinter tablePrinter;

    @BeforeEach
    void setUp() {
        tablePrinter = new MarkdownTablePrinter();
    }

    @Test
    void testPrintTable() throws CsvDataNotCorrectException {
        Table table = new BaseTable();

        table.addData(new String[]{"col1", "col2", "col3", "col4"});
        table.addData(new String[]{"value1", "value2", "value3", "value4"});
        table.addData(new String[]{"value5", "value6", "value7", "value8"});
        table.addData(new String[]{"value9", "value10", "value11", "value12"});

        Collection<String> result = tablePrinter.printTable(table,
                ColumnAlignment.LEFT, ColumnAlignment.CENTER, ColumnAlignment.RIGHT);

        Collection<String> expectedResult = new ArrayList<>();
        expectedResult.add("| col1   | col2    | col3    | col4    |");
        expectedResult.add("| :----- | :-----: | ------: | ------- |");
        expectedResult.add("| value1 | value2  | value3  | value4  |");
        expectedResult.add("| value5 | value6  | value7  | value8  |");
        expectedResult.add("| value9 | value10 | value11 | value12 |");

        assertIterableEquals(expectedResult, result,
                "Method printTable() should return the correct order of cells and columns!");
    }

}
