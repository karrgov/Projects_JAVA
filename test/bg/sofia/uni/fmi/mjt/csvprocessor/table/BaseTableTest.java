package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

public class BaseTableTest {

    private Table table;

    @BeforeEach
    void setUp() {
        table = new BaseTable();
    }

    @Test
    void testAddDataThrowsExceptionWhenDataIsNull() {
        assertThrows(IllegalArgumentException.class, () -> table.addData(null),
                "Method addData() should throw an exception if data is null!");
    }

    @Test
    void testAddDataThrowsExceptionForInvalidColumnsAmount() {
        table.addData(new String[]{"col1", "col2", "col3"});

        assertThrows(CsvDataNotCorrectException.class, () -> table.addData(new String[]{"val1", "val2"}),
                "Method addData() should throw  an exception if number of values does not match number of columns!");
    }

    @Test
    void testGetRowsCount() {
        assertEquals(0, table.getRowsCount(),
                "Method getRowsCount() should return 0 if no data has been added!");

        table.addData(new String[]{"val1_1", "val1_2", "val1_3"});
        table.addData(new String[]{"val2_1", "val2_2", "val2_3"});
        table.addData(new String[]{"val3_1", "val3_2", "val3_3"});

        assertEquals(3, table.getRowsCount(),
                "Method getRowsCount() should return the correct number if data has been added!");
    }

    @Test
    void testGetColumnNames() {
        table.addData(new String[]{"col1", "col2", "col3"});
        table.addData(new String[]{"val1_1", "val1_2", "val1_3"});
        Collection<String> actual = table.getColumnNames();

        Collection<String> expected = new LinkedHashSet<>();
        expected.add("col1");
        expected.add("col2");
        expected.add("col3");

        assertIterableEquals(expected, actual,
                "Method getColumnNames() should return correct names!");

        assertThrows(UnsupportedOperationException.class, () -> actual.add("test"),
                "Method getColumnNames() should return an unmodifiable collection!");
    }

    @Test
    void testGetColumnDataThrowsExceptionIfColumnIsNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData(null),
                "Method getColumnData() can not accept a null column!");

        assertThrows(IllegalArgumentException.class, () -> table.getColumnData("  "),
                "Method getColumnData() can not accept a blank column!");
    }

    @Test
    void testGetColumnDataThrowsExceptionIfColumnDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> table.getColumnData("parrot"),
                "Method getColumnData() should throw an exception if the given column does not exist!");
    }

    @Test
    void testGetColumnData() {
        table.addData(new String[]{"col1", "col2", "col3"});
        table.addData(new String[]{"val1_1", "val1_2", "val1_3"});
        table.addData(new String[]{"val2_1", "val2_2", "val2_3"});
        Collection<String> actual = table.getColumnData("col2");

        Collection<String> expected = new LinkedHashSet<>();
        expected.add("val1_2");
        expected.add("val2_2");

        assertIterableEquals(expected, actual,
                "Method getColumnData() should return correct values!");
    }

}
