package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.*;

public class BaseTable implements Table {

    private Map<String, Column> columns;
    private int rowSize;

    public BaseTable() {
        this(new LinkedHashMap<>(), 0);
    }

    public BaseTable(Map<String, Column> columns, int rowSize) {
        this.columns = columns;
        this.rowSize = rowSize;
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if(data == null) {
            throw new IllegalArgumentException("Data can not be null!");
        }

        if(this.columns.isEmpty()) {
            for(String str : data) {
                this.columns.put(str, null);
            }
        } else {
            Iterator<String> namesIterator = this.columns.keySet().iterator();

            for(String str : data) {
                if(!namesIterator.hasNext()) {
                    throw new CsvDataNotCorrectException("A row from the CSV file has more data than columns!");
                }

                String nameOfColumn = namesIterator.next();
                Column column = this.columns.get(nameOfColumn);

                if(column == null) {
                    column = new BaseColumn();
                    this.columns.put(nameOfColumn, column);
                }

                column.addData(str);
            }

            if(namesIterator.hasNext()) {
                throw new CsvDataNotCorrectException("A row from the CSV file has less data than columns!");
            }
        }
        this.rowSize++;
    }

    @Override
    public Collection<String> getColumnNames() {
        return Collections.unmodifiableSet(this.columns.keySet());
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if(column == null || column.isBlank()) {
            throw new IllegalArgumentException("Column can not be null or empty!");
        }

        if(!this.columns.containsKey(column)) {
            throw new IllegalArgumentException("The column does not exist in the table!");
        }

        Column result = this.columns.get(column);

        if(result == null) {
            return Collections.emptyList();
        }

        return this.columns.get(column).getData();
    }

    @Override
    public int getRowsCount() {
        return this.rowSize;
    }

}
