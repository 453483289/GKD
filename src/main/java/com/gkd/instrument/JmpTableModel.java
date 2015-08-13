package com.gkd.instrument;

import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.StringUtils;

import com.gkd.instrument.callgraph.JmpData;
import com.peterswing.CommonLib;

public class JmpTableModel extends DefaultTableModel {
	String columnNames[] = { "No.", "Date", "From", "To", "What", "Parameter", "Segment start", "Segment End", "eax", "ecx", "edx", "ebx", "esp", "ebp", "esi", "edi", "es", "cs",
			"ss", "ds", "fs", "gs", "stack", "stack base" };
	public Vector<JmpData> data = new Vector<JmpData>();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.S");

	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		try {
			return data.size();
		} catch (Exception ex) {
			return 0;
		}
	}

	public void setValueAt(Object aValue, int row, int column) {
	}

	public Object getValueAt(int row, int column) {
		//		try {
		JmpData jmpData = data.get(row);
		if (column == 0) {
			return jmpData.lineNo;
		} else if (column == 1) {
			return jmpData.date == null ? null : dateFormat.format(jmpData.date);
		} else if (column == 2) {
			if (jmpData.what != 0xffff) {
				Hashtable<String, Object> ht = new Hashtable<String, Object>();
				ht.put("address", jmpData.fromAddress);
				ht.put("DW_AT_name", StringUtils.defaultString(jmpData.fromAddress_DW_AT_name));
				ht.put("addressDescription", StringUtils.defaultString(jmpData.fromAddressDescription));
				ht.put("deep", jmpData.deep);
				return ht;
			} else {
				switch ((int) jmpData.fromAddress) {
				case 0x00:
					return "Division by zero";
				case 0x01:
					return "Debugger";
				case 0x02:
					return "NMI";
				case 0x03:
					return "Breakpoint";
				case 0x04:
					return "Overflow";
				case 0x05:
					return "Bounds";
				case 0x06:
					return "Invalid Opcode";
				case 0x07:
					return "Coprocessor not available";
				case 0x08:
					return "Double fault";
				case 0x09:
					return "Coprocessor Segment Overrun (386 or earlier only)";
				case 0x0A:
					return "Invalid Task State Segment";
				case 0x0B:
					return "Segment not present";
				case 0x0C:
					return "Stack Fault";
				case 0x0D:
					return "General protection fault";
				case 0x0E:
					return "Page fault";
				case 0x0F:
					return "reserved";
				case 0x10:
					return "Math Fault";
				case 0x11:
					return "Alignment Check";
				case 0x12:
					return "Machine Check";
				case 0x13:
					return "SIMD Floating-Point Exception";
				default:
					return "UNKNOWN : " + jmpData.fromAddress;
				}
			}
		} else if (column == 3) {
			if (jmpData.what != 0xffff) {
				Hashtable<String, Object> ht = new Hashtable<String, Object>();
				ht.put("address", jmpData.toAddress);
				ht.put("DW_AT_name", StringUtils.defaultString(jmpData.toAddress_DW_AT_name));
				ht.put("addressDescription", StringUtils.defaultString(jmpData.toAddressDescription));
				ht.put("deep", jmpData.deep);
				return ht;
			} else {
				return "Error code : " + jmpData.toAddress;
			}
		} else if (column == 4) {
			return jmpData.getWhatStr();
		} else if (column == 5) {
			return jmpData.parameters;
		} else if (column == 6) {
			return "0x" + Long.toHexString(jmpData.segmentStart);
		} else if (column == 7) {
			return "0x" + Long.toHexString(jmpData.segmentEnd);
		} else if (column == 8) {
			return "0x" + Long.toHexString(jmpData.eax);
		} else if (column == 9) {
			return "0x" + Long.toHexString(jmpData.ecx);
		} else if (column == 10) {
			return "0x" + Long.toHexString(jmpData.edx);
		} else if (column == 11) {
			return "0x" + Long.toHexString(jmpData.ebx);
		} else if (column == 12) {
			return "0x" + Long.toHexString(jmpData.esp);
		} else if (column == 13) {
			return "0x" + Long.toHexString(jmpData.ebp);
		} else if (column == 14) {
			return "0x" + Long.toHexString(jmpData.esi);
		} else if (column == 15) {
			return "0x" + Long.toHexString(jmpData.edi);
		} else if (column == 16) {
			return "0x" + Long.toHexString(jmpData.es);
		} else if (column == 17) {
			return "0x" + Long.toHexString(jmpData.cs);
		} else if (column == 18) {
			return "0x" + Long.toHexString(jmpData.ss);
		} else if (column == 19) {
			return "0x" + Long.toHexString(jmpData.ds);
		} else if (column == 20) {
			return "0x" + Long.toHexString(jmpData.fs);
		} else if (column == 21) {
			return "0x" + Long.toHexString(jmpData.gs);
		} else if (column == 22) {
			return CommonLib.arrayToHexString(jmpData.stack);
		} else if (column == 23) {
			return "0x" + Long.toHexString(jmpData.stackBase);
		} else {
			return "";
		}
		//		} catch (Exception ex) {
		//			ex.printStackTrace();
		//			return "";
		//		}
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void add(JmpData jmpDate) {
		data.add(jmpDate);
	}

	public void addAll(Vector<JmpData> d) {
		data.addAll(d);
	}

	public void removeAll() {
		data.clear();
		this.fireTableDataChanged();
	}
}
