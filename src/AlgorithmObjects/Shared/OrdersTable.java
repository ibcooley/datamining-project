package AlgorithmObjects.Shared;

import au.com.bytecode.opencsv.CSVReader;

import javax.swing.table.AbstractTableModel;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ben on 10/25/2014.
 * OrdersTable
 */
public class OrdersTable extends AbstractTableModel {

    private String[] columnNames;
    private List<String[]> rowData;

    private OrdersTable() {
        rowData = null;
        columnNames = null;
    }

    public OrdersTable(String csvFileName) throws IOException {
        this();
        CSVReader reader = new CSVReader(new FileReader(csvFileName));
        List<String[]> values = reader.readAll();
        int cols = values.get(0).length;
        columnNames = new String[cols];
        for (int i = 0; i < cols; i++) {
            columnNames[i] = values.get(0)[i];
        }
        for (int i = 1; i < values.size(); i++) {
            rowData.add(values.get(i));
        }
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public List<String[]> getRowData() {
        return rowData;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return rowData.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public String getValueAt(int row, int col) {
        return rowData.get(row)[col];
    }
}
