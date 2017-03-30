package com.uhg.optum.ssmo.esb.purgetool.service;

import java.sql.SQLException;

import com.uhg.optum.ssmo.esb.purgetool.dao.IPurgeToolDao;
import com.uhg.optum.ssmo.esb.purgetool.dao.PurgeToolDao;

public class PurgeToolService implements IPurgeToolService {

	@Override
	public void executeSQL(String sqlFileName, boolean isNew)
			throws SQLException {
		IPurgeToolDao dao = new PurgeToolDao();

		dao.resetDatabase(sqlFileName, isNew);

	}

}
