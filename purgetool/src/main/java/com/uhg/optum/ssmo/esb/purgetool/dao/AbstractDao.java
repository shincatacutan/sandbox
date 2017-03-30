package com.uhg.optum.ssmo.esb.purgetool.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.uhg.optum.ssmo.esb.purgetool.io.ConfigReader;

public class AbstractDao {
	public Connection getConnection(boolean isNew) throws SQLException {

		ConfigReader config = new ConfigReader();
		Connection conn = null;
		String jdbcURL;
		try {
			Class.forName(config.getPropValue("jdbc.driverClassName"));
			
			if (isNew) {
				jdbcURL = "jdbc.url.new";
			} else {
				jdbcURL = "jdbc.url.old";
			}
			String databaseUrl = config.getPropValue(jdbcURL);
			System.out.println("[INFO] Connecting to "+databaseUrl);
			
			conn = DriverManager.getConnection(databaseUrl, config.getPropValue("jdbc.username"),
					config.getPropValue("jdbc.password"));
		} catch (ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
		} catch (IOException e) {
			System.out.println("Error: unable to load property name!");
		}
		System.out.println("[INFO] Connected to database");
		return conn;
	}

}
