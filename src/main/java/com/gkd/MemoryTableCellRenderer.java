package com.gkd;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

public class MemoryTableCellRenderer extends JLabel implements TableCellRenderer {

	public MemoryTableCellRenderer() {
		super();
		this.setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		setFont(table.getFont());
		if (isSelected) {
			this.setBackground(table.getSelectionBackground());
		} else {
			if (row % 2 == 0) {
				this.setBackground(Color.white);
			} else {
				this.setBackground(new Color(0xf4f4f4));
			}
			if (column >= 0 && column <= 8) {
				this.setHorizontalAlignment(SwingConstants.CENTER);
			} else {
				this.setHorizontalAlignment(SwingConstants.LEFT);
			}
		}
		if (value != null) {
			this.setText(value.toString());
		}
		return this;
	}
}
