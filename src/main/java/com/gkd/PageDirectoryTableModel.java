package com.gkd;

import javax.swing.table.DefaultTableModel;

public class PageDirectoryTableModel extends DefaultTableModel {
	String columnNames[] = new String[] { "No.", "PT base", "AVL", "G", "PS", "D", "A", "PCD", "PWT", "U/S", "W/R", "P" };
	boolean showZeroAddress = true;

	public boolean isShowZeroAddress() {
		return showZeroAddress;
	}

	public void setShowZeroAddress(boolean showZeroAddress) {
		this.showZeroAddress = showZeroAddress;
		this.fireTableStructureChanged();
		this.fireTableDataChanged();
	}

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if (showZeroAddress) {
			return super.getRowCount();
		} else {
			int count = 0;
			for (int x = 0; x < super.getRowCount(); x++) {
				if (!super.getValueAt(x, 1).toString().equals("0x0")) {
					count++;
				}
			}
			return count++;
		}
	}

	public Object getValueAt(int row, int column) {
		if (showZeroAddress) {
			return super.getValueAt(row, column);
		} else {
			int temp = 0;
			for (int x = 0; x < super.getRowCount(); x++) {
				if (!super.getValueAt(x, 1).toString().equals("0x0")) {
					if (temp == row) {
						return super.getValueAt(x, column);
					} else {
						temp++;
					}
				}
			}
			return "ERROR";
		}
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
