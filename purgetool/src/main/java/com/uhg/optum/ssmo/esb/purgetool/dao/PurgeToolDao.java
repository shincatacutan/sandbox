package com.uhg.optum.ssmo.esb.purgetool.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PurgeToolDao extends AbstractDao implements IPurgeToolDao {

	public void resetDatabase(String sqlScript, boolean isNew) throws SQLException {

		StringBuffer sb = new StringBuffer();
		Connection c = null;
		try {
			ClassLoader classLoader  = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream(sqlScript);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				sb.append(sCurrentLine);
			}

			br.close();

			String[] inst = sb.toString().split(";");

			c = getConnection(isNew);
			Statement st = c.createStatement();
			c.setAutoCommit(false);
			for (int i = 0; i < inst.length; i++) {

				if (!inst[i].trim().equals("")) {
					st.executeUpdate(inst[i]);
					System.out.println(">> " + inst[i]);
				}
			}
			c.commit();
			System.out.println("[INFO] All transactions are committed!");
			
		} catch (Exception e) {
			c.rollback();
			System.out.println("*** Error : " + e.toString());
			System.out.println("*** ");
			System.out.println("*** Error : ");
			e.printStackTrace();
			System.out.println("################################################");
		}

	}
	
}
