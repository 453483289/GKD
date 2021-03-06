package com.gkd;

import java.awt.BorderLayout;
import java.math.BigInteger;
import java.util.LinkedHashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.gkd.architecture.DescriptorParser;
import com.gkd.stub.VMController;
import com.peterswing.CommonLib;

public class IDTDescriptorPanel extends JPanel {
	private JTable byteTable;
	private int idtNo;
	private JPanel panel3;
	private JPanel panel2;
	private JTabbedPane tabbedPane1;
	private JLabel typeLabel;
	private JTable fieldTable;
	private JScrollPane scrollPane1;
	private int b[];
	private long value;
	private long bit[] = new long[64];
	private GKD gkd;
	private BigInteger idtAddress;
	private JScrollPane scrollPane2;

	public IDTDescriptorPanel(GKD gkd, BigInteger idtAddress, int idtNo) {
		this.gkd = gkd;
		this.idtAddress = idtAddress;
		this.idtNo = idtNo;

		initGUI();
	}

	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				this.setLayout(thisLayout);
			}
			{
				tabbedPane1 = new JTabbedPane();
				this.add(tabbedPane1, BorderLayout.CENTER);
				tabbedPane1.setTabPlacement(JTabbedPane.LEFT);
				tabbedPane1.setPreferredSize(new java.awt.Dimension(515, 437));
				{
					panel2 = new JPanel();
					tabbedPane1.addTab(MyLanguage.getString("Info"), null, panel2, null);
					BorderLayout jPanel2Layout = new BorderLayout();
					panel2.setLayout(jPanel2Layout);
					{
						scrollPane1 = new JScrollPane();
						panel2.add(scrollPane1, BorderLayout.CENTER);
						scrollPane1.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
						{
							DefaultTableModel jTable1Model = new DefaultTableModel(new String[][] { { "" }, { "" } }, new String[] { "31" });
							for (int x = 30; x >= 0; x--) {
								jTable1Model.addColumn(x);
							}

							byteTable = new JTable();
							scrollPane1.setViewportView(byteTable);
							byteTable.setModel(jTable1Model);
							byteTable.setBounds(12, 12, 562, 50);
						}
					}
					{
						typeLabel = new JLabel();
						panel2.add(typeLabel, BorderLayout.NORTH);
						typeLabel.setText("Type : ");
					}
				}
				{
					panel3 = new JPanel();
					tabbedPane1.addTab(MyLanguage.getString("Field"), null, panel3, null);
					BorderLayout jPanel3Layout = new BorderLayout();
					panel3.setLayout(jPanel3Layout);
					{
						scrollPane2 = new JScrollPane();
						panel3.add(scrollPane2, BorderLayout.CENTER);
						{
							TableModel jTable2Model = new DefaultTableModel(new String[][] {}, new String[] { MyLanguage.getString("Field"), MyLanguage.getString("Value") });
							fieldTable = new JTable();
							scrollPane2.setViewportView(fieldTable);
							fieldTable.setModel(jTable2Model);
						}
					}
				}
			}
			// GKD.commandReceiver.setCommandNoOfLine(2);
			//			String result;
			//			GKD.sendBochsCommand("info idt " + idtNo);
			//			String idtNoHex = String.format("0x%02x", idtNo);
			//			result = GKD.commandReceiver.getCommandResult("IDT[" + idtNoHex + "]");

			//			GKD.commandReceiver.clearBuffer();
			//			GKD.sendBochsCommand("x /8bx " + String.format("0x%08x", idtAddress.add(BigInteger.valueOf(idtNo * 8))));
			//			result = GKD.commandReceiver.getCommandResult(String.format("%08x", idtAddress.add(BigInteger.valueOf(idtNo * 8))));
			//			logger.debug("idtNo=" + idtNo + " , idtAddress+" + idtAddress + " , " + (idtAddress.add(BigInteger.valueOf(idtNo * 8))));
			//			logger.debug(result);
			//			String lines[] = result.split("\n");
			//
			//			String byteStr[] = lines[0].replaceFirst("^.*>:\t", "").split("\t");
			//			for (int x = 0; x < 8; x++) {
			//				b[x] = (byte) Long.parseLong(byteStr[x].substring(2), 16);
			//			}

			b = VMController.getVM().virtualMemory(idtAddress.add(BigInteger.valueOf(idtNo * 8)), 8);

			value = CommonLib.getLong(b, 0);

			for (int x = 0; x < 64; x++) {
				bit[x] = CommonLib.getBit(value, x);
			}

			for (int x = 0; x < 32; x++) {
				byteTable.setValueAt(value >> x & 1, 1, 31 - x);
			}

			for (int x = 32; x < 64; x++) {
				byteTable.setValueAt(value >> x & 1, 0, 63 - x);
			}

			// parse descriptor
			if (bit[40] == 0 && bit[41] == 1 && bit[42] == 1 && bit[44] == 0) {
				typeLabel.setText("Type : Interrupt gate descriptor, value=0x" + Long.toHexString(value));
				parseInterruptGateDescriptor();
			} else if (bit[40] == 1 && bit[41] == 0 && bit[42] == 1 && bit[43] == 0 && bit[44] == 0) {
				typeLabel.setText("Type : Task gate descriptor, value=0x" + Long.toHexString(value));
				parseTaskGateDescriptor();
			} else if (bit[44] == 0 && bit[43] == 0 && bit[42] == 0 && bit[41] == 1 && bit[40] == 0) {
				typeLabel.setText("Type : LDT descriptor, value=0x" + Long.toHexString(value) + ", base=0x" + Long.toHexString(CommonLib.getInt(b[2], b[3], b[4], b[7]))
						+ ", limit=0x" + Long.toHexString(CommonLib.getShort(b[0], b[1])));
				parseLDT();
			} else if (bit[44] == 0 && bit[42] == 0 && bit[40] == 1) {
				typeLabel.setText("Type : TSS descriptor, value=0x" + Long.toHexString(value));
				this.removeAll();
				this.setLayout(new BorderLayout());
				this.add(new TSSPanel(gkd, 2, idtAddress, idtNo, b), BorderLayout.CENTER);
			}
			// end parse descriptor
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseInterruptGateDescriptor() {
		try {
			DefaultTableModel model = (DefaultTableModel) fieldTable.getModel();
			long cs_selector = CommonLib.getLong(b[2], b[3], 0, 0, 0, 0, 0, 0);
			model.addRow(new String[] { "cs selector", "0x" + Long.toHexString(cs_selector) + " (0x" + Long.toHexString(cs_selector >> 3) + ")" });

			long offset = CommonLib.getLong(b[0], b[1], b[6], b[7], 0, 0, 0, 0);
			model.addRow(new String[] { "offset", "0x" + Long.toHexString(offset) });

			model.addRow(new String[] { "P", String.valueOf(bit[47]) });
			model.addRow(new String[] { "DPL", String.valueOf(bit[46] << 8 + bit[45]) });
			model.addRow(new String[] { "S", String.valueOf(bit[44]) });
			model.addRow(new String[] { "D", String.valueOf(bit[43]) });
			model.addRow(new String[] { "G", String.valueOf(bit[42]) });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void parseTaskGateDescriptor() {
		try {
			DefaultTableModel model = (DefaultTableModel) fieldTable.getModel();
			long tss_selector = CommonLib.getLong(b[2], b[3], 0, 0, 0, 0, 0, 0);
			model.addRow(new String[] { "tss selector", "0x" + Long.toHexString(tss_selector) + " (0x" + Long.toHexString(tss_selector >> 3) + ")" });

			model.addRow(new String[] { "P", String.valueOf(bit[47]) });
			model.addRow(new String[] { "DPL", String.valueOf(bit[46] << 8 + bit[45]) });
			model.addRow(new String[] { "S", String.valueOf(bit[44]) });
			model.addRow(new String[] { "G", String.valueOf(bit[42]) });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void parseLDT() {
		try {
			DefaultTableModel model = (DefaultTableModel) fieldTable.getModel();

			BigInteger base = BigInteger.valueOf(CommonLib.getInt(b[2], b[3], b[4], b[7]));
			long limit = CommonLib.getShort(b[0], b[1]);
			model.addRow(new String[] { "base", "0x" + base.toString(16) });
			model.addRow(new String[] { "limit", "0x" + Long.toHexString(limit) });
			model.addRow(new String[] { "dpl", "0x" + Long.toHexString(bit[46] << 1 + bit[45]) });
			model.addRow(new String[] { "p", "0x" + Long.toHexString(bit[47]) });
			model.addRow(new String[] { "avl", "0x" + Long.toHexString(bit[52]) });
			model.addRow(new String[] { "g", "0x" + Long.toHexString(bit[55]) });

			// parse each descriptor

			JScrollPane pane = new JScrollPane();
			tabbedPane1.addTab(MyLanguage.getString("Descriptor"), null, pane, null);
			JTable table = new JTable();
			DefaultTableModel model2 = new DefaultTableModel(new String[][] {},
					new String[] { "No.", "Type", "Value", "Base", "Limit", "A", "R/W", "C/E", "X", "S", "DPL", "P", "AVL", "D/B", "G" });

			if (limit > 1000) {
				limit = 1000;
			}
			int bytes[] = VMController.getVM().virtualMemory(base, (int) limit + 1); //GKD.getLinearMemory(base, (int) (limit + 1));

			for (int x = 0; x < limit; x += 8) {
				long value = CommonLib.getLong(bytes, x);
				LinkedHashMap<String, String> hm = DescriptorParser.parseDescriptor(value);
				model2.addRow(new String[] { String.valueOf(x), hm.get("Type"), hm.get("Value"), hm.get("Base"), hm.get("Limit"), hm.get("A"), hm.get("R/W"), hm.get("C/E"),
						hm.get("X"), hm.get("S"), hm.get("DPL"), hm.get("P"), hm.get("AVL"), hm.get("D/B"), hm.get("G") });
			}
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			table.setModel(model2);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			for (int x = 5; x <= 14; x++) {
				table.getColumnModel().getColumn(x).setPreferredWidth(50);
			}
			pane.setViewportView(table);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}