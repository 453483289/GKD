package com.gkd;

import java.math.BigInteger;

public class Global {
	public static boolean debug;
	public static BigInteger osDebug = BigInteger.valueOf(-1);
	public static String jmpLog = "jmp.log";
	public static String interruptLog = "interrupt.log";
	public static int profilingMemoryPort = 1980;
	public static int profilingJmpPort = 1981;
	public static int profilingInterruptPort = 1982;
	public static String bits = "32bits";
	public static String elfPaths[];
	public static int clickedWhichInstructionPanel;
	public static String peterBochsCacheDir = "peter-bochs-cache";
	public static boolean isBeta = false;
	public static String lastCommand;
	public static int MAX_NEXTI_INSTRUCTION_COUNT = 10;
	public static String stopCommand;
	public static String ndisasmPath;
	public static boolean showDebugLoc;
	public static boolean showDebugInfoEntriesInCompileUnit;
}
