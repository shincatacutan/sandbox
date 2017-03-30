package com.uhg.optum.ssmo.esb.purgetool.app;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.uhg.optum.ssmo.esb.purgetool.io.ConfigReader;
import com.uhg.optum.ssmo.esb.purgetool.service.IPurgeToolService;
import com.uhg.optum.ssmo.esb.purgetool.service.PurgeToolService;
import com.uhg.optum.ssmo.esb.purgetool.ssh.JSchConnector;

public class PurgeTool {
	public static void main(String[] args) {
		boolean isNew = false;
		if (args.length > 0) {
			String param = args[0];

			if (param.toLowerCase().equals("1bf") || param.toLowerCase().equals("newbf")) {
				isNew = true;
			}
			// else if no param, old bf will be purged
		}
		new PurgeTool().execute(isNew);
	}

	private void execute(boolean newDB) {
		System.out.println("====================== PURGE TOOL =========================");
		long startTime = System.currentTimeMillis();

		try {
			connectToSSH(newDB);
			System.out.println("[INFO] Resetting the database...");
			resetDatabase(newDB);
		} catch (IOException e) {
			System.out.println("[INFO] Error reading config file. " + e.getMessage());
			e.printStackTrace();
		} catch (JSchException e) {
			System.out.println("[INFO] Command execution exception " + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("[INFO] SQL exception " + e.getMessage());
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		long secondsRaw = duration / 1000;
		int minutes = (int) secondsRaw / 60;
		double seconds = secondsRaw % 60;

		System.out.println("[INFO] Time elapsed: " + minutes + " mins " + seconds + " secs");
		System.out.println("====================== END OF EXECUTION =========================");
	}

	private void connectToSSH(boolean isNewDB) throws IOException, JSchException {
		ConfigReader config = new ConfigReader();

		String username;
		String hostname;
		String password;
		String command;

		hostname = config.getPropValue("hostname");
		username = config.getPropValue("username");
		password = config.getPropValue("password");
		if (isNewDB) {
			System.out.println("[INFO] ebmmr1bf (new breakfix) will be purged.");
			command = "cd /ebill/exports/ebmmr1bf"
					+ "\n"
					+ ". /bronzbf/sqllib/db2profile"
					+ "\n"
					+ "db2 -tf load.mmr1bf.-4tables.scr";
		} else {
			System.out.println("[INFO] ebmmrbf (old breakfix) will be purged.");
			command = "cd /ebill/exports/ebmmrbf"
					+ "\n"
					+ ". /bron9bf/sqllib/db2profile"
					+ "\n"
					+ "db2 -tf load.mmrbf.-4tables.scr";
		}
		System.out.println("[INFO] Connecting to " + hostname + " using username: " + username);
		System.out.println("[INFO] Command: ");
		System.out.println(command);
		Session session = new JSchConnector().getSession(hostname, username, password);
		if (null != session) {
			System.out.println("[INFO] Session has been established.");
		}
		Channel channel = session.openChannel("exec");

		((ChannelExec) channel).setCommand(command);
		channel.setInputStream(null);
		((ChannelExec) channel).setErrStream(System.err);

		InputStream in = channel.getInputStream();

		channel.connect();

		byte[] tmp = new byte[1024];
		while (true) {
			while (in.available() > 0) {
				int i = in.read(tmp, 0, 1024);
				if (i < 0)
					break;
				System.out.print(new String(tmp, 0, i));
			}
			if (channel.isClosed()) {
				if (in.available() > 0)
					continue;
				System.out.println("[INFO] exit-status: " + channel.getExitStatus());
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
			}
		}

		channel.disconnect();
		session.disconnect();

	}

	public void resetDatabase(boolean isNew) throws SQLException {
		IPurgeToolService service = new PurgeToolService();
		service.executeSQL("mmr-basetables.sql", isNew);
	}

}
