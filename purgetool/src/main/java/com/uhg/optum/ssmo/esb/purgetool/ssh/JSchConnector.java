package com.uhg.optum.ssmo.esb.purgetool.ssh;

import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class JSchConnector {

	public Session getSession(String hostname, String username, String password) {
		Session session = null;
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, hostname, 22);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setPassword(password);
			session.connect();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return session;
	}
}