package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;

public class BaseColumnTest {

    private Column column;

    @BeforeEach
    void setUp() {
        column = new BaseColumn();
    }

    @Test
    void testAddDataThrowsExceptionWhenDataIsNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> column.addData(null),
                "Method addData() should throw IllegalArgumentException when data is null!");

        assertThrows(IllegalArgumentException.class, () -> column.addData("  "),
                "Method addData() should throw IllegalArgumentException when data is blank!");
    }

    @Test
    void testAddData() {
        assertDoesNotThrow(() -> column.addData("testData"),
                "Method addData() should not throw an exception when it receives the correct data!");

        assertTrue(column.getData().contains("testData"),
                "Data has to be in the column!");
    }

    @Test
    void testGetData() {
        column.addData("test1");
        column.addData("test2");
        column.addData("test3");

        Collection<String> result = column.getData();

        assertFalse(result.isEmpty(),
                "The collection returned by getData() should not be empty after adding elements to it!");

        Collection<String> testCollection = new LinkedHashSet<>();

        testCollection.add("test1");
        testCollection.add("test2");
        testCollection.add("test3");

        assertIterableEquals(testCollection, result,
                "Correct collection should be returned!");

        assertThrows(UnsupportedOperationException.class, () -> result.add("test4"),
                "An unmodifiable collection should not be able to receive any more elements!");
    }

}
