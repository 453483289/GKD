package com.gkd;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class HistoryTableCellRenderer extends JLabel implements TableCellRenderer {

	public HistoryTableCellRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setFont(table.getFont());
		if (value == null) {
			this.setText("");
		} else {
			this.setText(value.toString());
		}
		if (isSelected) {
			this.setBackground(table.getSelectionBackground());
		} else {
			if (row % 2 == 0) {
				this.setBackground(Color.white);
			} else {
				this.setBackground(new Color(0xf4f4f4));
			}
		}
		//		if (column >= 5) {
		//			setHorizontalAlignment(JLabel.CENTER);
		//		} else {
		setHorizontalAlignment(JLabel.LEFT);
		//		}
		return this;
	}

}
