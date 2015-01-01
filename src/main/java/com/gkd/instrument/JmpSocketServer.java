package com.gkd.instrument;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.gkd.Global;
import com.gkd.instrument.callgraph.JmpData;
import com.gkd.sourceleveldebugger.SourceLevelDebugger;
import com.peterdwarf.elf.Elf32_Sym;
import com.peterswing.CommonLib;

public class JmpSocketServer implements Runnable {
	private int port;
	private JmpTableModel jmpTableModel;
	private boolean shouldStop;
	private ServerSocket serverSocket;
	//	FileWriter fstream;

	//	public static LinkedHashSet<String> segments = new LinkedHashSet<String>();
	public static Vector<JmpData> jmpDataVector = new Vector<JmpData>();

	public void startServer(int port, JmpTableModel jmpTableModel) {
		this.port = port;
		this.jmpTableModel = jmpTableModel;
		//		try {
		//			fstream = new FileWriter(Global.jmpLog, true);
		//		} catch (IOException e1) {
		//			e1.printStackTrace();
		//		}

		shouldStop = false;
		new Thread(this).start();

		while (serverSocket != null && !serverSocket.isBound()) {
			try {
				Thread.currentThread().sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopServer() {
		shouldStop = true;
		try {
			serverSocket.close();
		} catch (Exception e) {
		}
	}

	@Override
	public void run() {
		if (Global.debug) {
			System.out.println("Jmp server start at port " + port);
		}

		try {
			serverSocket = new ServerSocket(port);
			while (!shouldStop) {
				Socket clientSocket = serverSocket.accept();
				DataInputStream in = new DataInputStream(clientSocket.getInputStream());

				int lineNo = 1;

				while (!shouldStop) {
					//					int length = (int) CommonLib.readLongFromInputStream(in);
					//					byte bytes[] = new byte[length];
					//					in.read(bytes);
					//					//System.out.println(new String(bytes));

					long fromAddress = CommonLib.readLong64BitsFromInputStream(in);
					long toAddress = CommonLib.readLong64BitsFromInputStream(in);
					long segmentStart = CommonLib.readLongFromInputStream(in);
					long segmentEnd = CommonLib.readLongFromInputStream(in);

					long eax = CommonLib.readLongFromInputStream(in);
					long ecx = CommonLib.readLongFromInputStream(in);
					long edx = CommonLib.readLongFromInputStream(in);
					long ebx = CommonLib.readLongFromInputStream(in);
					long esp = CommonLib.readLongFromInputStream(in);
					long ebp = CommonLib.readLongFromInputStream(in);
					long esi = CommonLib.readLongFromInputStream(in);
					long edi = CommonLib.readLongFromInputStream(in);

					long es = CommonLib.readShortFromInputStream(in);
					long cs = CommonLib.readShortFromInputStream(in);
					long ss = CommonLib.readShortFromInputStream(in);
					long ds = CommonLib.readShortFromInputStream(in);
					long fs = CommonLib.readShortFromInputStream(in);
					long gs = CommonLib.readShortFromInputStream(in);

					synchronized (jmpDataVector) {
						Elf32_Sym symbol = SourceLevelDebugger.symbolTableModel.searchSymbol(fromAddress);
						String fromAddressDescription = symbol == null ? null : symbol.name;
						symbol = SourceLevelDebugger.symbolTableModel.searchSymbol(toAddress);
						String toAddressDescription = symbol == null ? null : symbol.name;

						jmpDataVector.add(new JmpData(lineNo, new Date(), fromAddress, fromAddressDescription, toAddress, toAddressDescription, segmentStart, segmentEnd, eax, ecx,
								edx, ebx, esp, ebp, esi, edi, es, cs, ss, ds, fs, gs));
					}

					//					fstream.write(lineNo + "-" + dateformat1.format(new Date()) + "-" + fromAddress + "-" + toAddress + "-" + segmentStart + "-" + segmentEnd + "\n");

					lineNo++;
				}

				in.close();
				clientSocket.close();
			}
			serverSocket.close();
		} catch (BindException ex) {
			JOptionPane.showMessageDialog(null, "You have turn on the profiling feature but the port " + port + " is not available. Program exit", "Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (IOException ex2) {

		}
	}
}
