package com.gkd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

import org.apache.commons.io.FileUtils;

import com.gkd.GeneralKernelDebugger.OSType;
import com.peterswing.CommonLib;

public class Disassemble {
	public static String disassemble(BigInteger address, int bits) {
		try {
			int bytes[] = GKDCommonLib.getMemoryFromBochs(address, 100);
			FileUtils.writeByteArrayToFile(new File("temp"), CommonLib.intArrayToByteArray(bytes));

			ProcessBuilder pb;
			if (GeneralKernelDebugger.os == OSType.mac || GeneralKernelDebugger.os == OSType.linux) {
				pb = new ProcessBuilder("ndisasm", "-b " + bits, "temp");
			} else {
				pb = new ProcessBuilder("ndisasm.exe", "-b " + bits, "temp");
			}

			pb.redirectErrorStream(true);
			Process p;

			p = pb.start();
			String str = "";
			InputStream is = p.getInputStream();
			final BufferedReader br = new BufferedReader(new InputStreamReader(is), 1024);
			String line;
			while ((line = br.readLine()) != null) {
				str += line + "\n";
			}
			new File("temp").delete();
			return str;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
