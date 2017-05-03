package similarity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class AgeSimTableModel  extends AbstractTableModel  {
	Vector<String> columnNames = new Vector<String>();
	Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	
	public AgeSimTableModel() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("age_similarities.csv"));
			String line;
			String[] cells;
			Vector<Object> vector;
			while((line = reader.readLine()) != null) {
				cells = line.split(",");
				if (columnNames.size() == 0) {
					for (int i=0; i<cells.length; i++) {
						columnNames.add(cells[i].trim());
					}
				}
				else {
					vector = new Vector<Object>();
					vector.add(cells[0].trim());
					for (int i=1; i<cells.length; i++) {
						vector.add(Double.parseDouble(cells[i]));
					}
					data.add(vector);						
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getRowCount() {
		return data.size();
	}
	@Override
	public int getColumnCount() {
		return columnNames.size();
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex).get(columnIndex);
	}
	@Override
	public String getColumnName(int columnIndex) {
	    return columnNames.get(columnIndex);
	}
	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex > 0;
	}
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		double converted = (double) value;
		if (converted >= 0 && converted <= 1) {
			data.get(rowIndex).set(columnIndex, value);
			fireTableCellUpdated(rowIndex, columnIndex);				
		}
		else {
			System.out.println("Inkorrekt värde: " + converted);
		}	
	}
	
	public void writeFile() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("age_similarities.csv"));
			for (String columnName : columnNames) {
				writer.write(columnName + ",");
			}
			writer.newLine();
			for (Vector<Object> vector : data) {
				for (Object obj : vector) {
					String x = obj.toString() + ",";
					writer.write(x);
				}
				writer.newLine();
			}
			writer.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


}
