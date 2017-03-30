package com.uhg.optum.ssmo.esb.purgetool.service;

import java.sql.SQLException;

public interface IPurgeToolService {

	void executeSQL(String sqlFileName, boolean isNew) throws SQLException;
}
