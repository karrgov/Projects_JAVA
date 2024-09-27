package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvProcessor implements CsvProcessorAPI {

    private final Table table;

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        if(reader == null) {
            throw new IllegalArgumentException("Reader can not be null!");
        }

        if(delimiter.isBlank()) {
            throw new IllegalArgumentException("Delimiter can not be blank!");
        }

        delimiter = "\\Q" + delimiter + "\\E";
        String line;

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(delimiter);

                if(!isFormatCorrect(data)) {
                    throw new CsvDataNotCorrectException("CSV data format is wrong!");
                }

                table.addData(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while processing the CSV file!", e);
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        if(writer == null) {
            throw new IllegalArgumentException("Writer can not be null!");
        }

        PrintWriter printWriter = new PrintWriter(writer);

        TablePrinter printer = new MarkdownTablePrinter();
        Collection<String> rows = printer.printTable(table, alignments);

        Iterator<String> iterator = rows.iterator();

        while(iterator.hasNext()) {
            String currentRow = iterator.next();

            if(iterator.hasNext()) {
                printWriter.println(currentRow);
            } else {
                printWriter.print(currentRow);
            }
        }
    }

    private boolean isFormatCorrect(String[] data) {
        String REGEX = "^[a-zA-Z0-9]*$";
        Pattern PATTERN = Pattern.compile(REGEX);

        for(String str : data) {
            Matcher matcher = PATTERN.matcher(str);
            boolean result = matcher.matches();
            if(!result) {
                return false;
            }
        }
        return true;
    }

}
