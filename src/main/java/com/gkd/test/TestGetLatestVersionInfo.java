package com.gkd.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.gkd.GKDCommonLib;

public class TestGetLatestVersionInfo {

	public static void main(String[] args) {
		HashMap<String, String> map = GKDCommonLib.checkLatestVersion();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			System.out.println(pairs.getKey() + " = " + pairs.getValue());
		}
	}

}
