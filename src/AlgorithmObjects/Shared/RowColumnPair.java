package AlgorithmObjects.Shared;

/**
 * Created by Ben on 10/25/2014.
 * RowColumnPair
 */
public class RowColumnPair {

    private final String column;
    private final String row;

    public RowColumnPair(String columnValue, String rowValue) {
        this.column = columnValue;
        this.row = rowValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RowColumnPair that = (RowColumnPair) o;

        return column.equals(that.column) && row.equals(that.row);

    }

    @Override
    public int hashCode() {
        int result = column.hashCode();
        result = 31 * result + row.hashCode();
        return result;
    }
}
