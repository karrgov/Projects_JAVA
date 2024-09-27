package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CsvProcessorTest {

//    @Mock
//    private Table tableMock;
    private Table tableMock = Mockito.mock();

//    @InjectMocks
//    private static CsvProcessor csvProcessor;
    private CsvProcessor csvProcessor = new CsvProcessor(tableMock);

//    @BeforeAll
//    static void setUp() {
//        csvProcessor = new CsvProcessor();
//    }

    @Test
    void testReadCsvThrowsExceptionIfReaderIsNullOrDelimiterIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> csvProcessor.readCsv(null, ","),
                "Method readCsv() can not accept a null reader!");

        assertThrows(IllegalArgumentException.class, () -> csvProcessor.readCsv(new StringReader("str"), "  "),
                "Method readCsv() can not accept a blank delimiter!");
    }

    @Test
    void testReadCsvThrowsExceptionIfFormatIsNotCorrect() {
        String csvContent = "Name,A*#e,Car" + System.lineSeparator() + "Tom,30,Merc&$^des" + System.lineSeparator() + "Jane,29,BMW";
        StringReader stringReader = new StringReader(csvContent);

        assertThrows(CsvDataNotCorrectException.class, () -> csvProcessor.readCsv(stringReader, ","),
                "Method readCsv() should throw an exception if format is incorrect!");
    }

    @Test
    void testReadCsv() {
        String csvContent = "Name,Age,Car" + System.lineSeparator() + "Tom,30,Mercedes" + System.lineSeparator() + "Jane,29,BMW";
        StringReader stringReader = new StringReader(csvContent);

        try {
            csvProcessor.readCsv(stringReader, ",");
        } catch (CsvDataNotCorrectException e) {
            fail("Unexpected exception was thrown!");
        }

        verify(tableMock).addData(new String[]{"Name", "Age", "Car"});
        verify(tableMock).addData(new String[]{"Tom", "30", "Mercedes"});
        verify(tableMock).addData(new String[]{"Jane", "29", "BMW"});
    }

    @Test
    void testWriteTableThrowsExceptionIfWriterIsNull() {
        assertThrows(IllegalArgumentException.class, () -> csvProcessor.writeTable(null),
                "Method writeTable() can not accept a null writer!");
    }

//    @Disabled
    @Test
    void testWriteTable() {
        when(tableMock.getRowsCount()).thenReturn(3);
        when(tableMock.getColumnNames()).thenReturn(List.of("Name", "Age", "Car"));
        when(tableMock.getColumnData("Name")).thenReturn(List.of("Tom", "Jane"));
        when(tableMock.getColumnData("Age")).thenReturn(List.of("30", "29"));
        when(tableMock.getColumnData("Car")).thenReturn(List.of("Mercedes", "BMW"));

        StringWriter stringWriter = new StringWriter();

        csvProcessor.writeTable(stringWriter, ColumnAlignment.LEFT, ColumnAlignment.RIGHT, ColumnAlignment.CENTER);

        String expectedOutput =
        "| Name | Age | Car      |" + System.lineSeparator() +
        "| :--- | --: | :------: |" + System.lineSeparator() +
        "| Tom  | 30  | Mercedes |" + System.lineSeparator() +
        "| Jane | 29  | BMW      |";

        assertEquals(expectedOutput, stringWriter.toString(),
                "Table output is not correct!");
    }

}
