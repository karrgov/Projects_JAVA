package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import java.util.*;

public class BaseColumn implements Column {

    private Set<String> values;

    public BaseColumn() {
        this(new LinkedHashSet<>());
    }

    public BaseColumn(Set<String> values) {
        this.values = values;
    }

    @Override
    public void addData(String data) {
        if(data == null || data.isBlank()) {
            throw new IllegalArgumentException("Data can not be null or blank!");
        }

        this.values.add(data);
    }

    @Override
    public Collection<String> getData() {
        return Collections.unmodifiableSet(this.values);
    }

}
