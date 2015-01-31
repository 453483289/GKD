package com.gkd.instrument;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.h2.tools.DeleteDbFiles;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.gkd.hibernate.HibernateUtil;
import com.gkd.instrument.callgraph.JmpData;

public class DBThread implements Runnable {
	//	Connection conn;
	//	Statement stat;
	//	PreparedStatement pstmt = null;
	//	private static Session session = HibernateUtil.openSession();
	public static Logger logger = Logger.getLogger(JmpSocketServer.class);

	@Override
	public void run() {
		try {
			//			Class.forName("org.h2.Driver");
			//			conn = DriverManager.getConnection("jdbc:h2:" + new File(".").getAbsolutePath() + "/jmpDB");
			//			stat = conn.createStatement();
			//			stat.execute("create table table1(jmpDataId integer generated by default as identity, cs bigint not null, date timestamp, name varchar(255))");
			int count = 0;
			while (true) {
				//				Transaction tx = session.beginTransaction();
				//				Iterator<JmpData> iterator = JmpSocketServer.jmpDataVector.iterator();
				Class.forName("org.h2.Driver");
				Connection conn = DriverManager.getConnection("jdbc:h2:" + new File(".").getAbsolutePath() + "/jmpDB");
				PreparedStatement pstmt = conn
						.prepareStatement("insert into jmpData (jmpDataId, cs, date, deep, ds, eax, ebp, ebx, ecx, edi, edx, es, esi, esp, fromAddress, fromAddressDescription, fs, gs, lineNo, segmentEnd, segmentStart, ss, toAddress, toAddressDescription, what) values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

				for (JmpData jmpData : JmpSocketServer.jmpDataVector) {
					//				while (iterator.hasNext()) {
					//					JmpData jmpData = iterator.next();
					//session.save(jmpData);
					//					iterator.remove();

					pstmt.setLong(1, jmpData.cs);
					pstmt.setDate(2, new java.sql.Date(jmpData.date.getTime()));
					pstmt.setLong(3, jmpData.deep);
					pstmt.setLong(4, jmpData.ds);
					pstmt.setLong(5, jmpData.eax);
					pstmt.setInt(6, 12345);
					pstmt.setInt(7, 12345);
					pstmt.setInt(8, 12345);
					pstmt.setInt(9, 12345);
					pstmt.setInt(10, 12345);
					pstmt.setInt(11, 12345);
					pstmt.setInt(12, 12345);
					pstmt.setInt(13, 12345);
					pstmt.setInt(14, 12345);
					pstmt.setString(15, "cheung");
					pstmt.setInt(16, 12345);
					pstmt.setInt(17, 12345);
					pstmt.setInt(18, 12345);
					pstmt.setInt(19, 12345);
					pstmt.setInt(20, 12345);
					pstmt.setInt(21, 12345);
					pstmt.setInt(22, 12345);
					pstmt.setString(23, "peter");
					pstmt.setInt(24, 10);

					pstmt.addBatch();

					JmpSocketServer.jmpDataVector.remove(jmpData);
					count++;
					if (count % 100000 == 0) {
						logger.info("writted to db = " + count + ", " + JmpSocketServer.jmpDataVector.size());
					}
				}
				pstmt.executeBatch();
				conn.close();
				//				tx.commit();
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
