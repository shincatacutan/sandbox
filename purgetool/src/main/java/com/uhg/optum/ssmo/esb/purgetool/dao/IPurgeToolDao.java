package com.uhg.optum.ssmo.esb.purgetool.dao;

import java.sql.SQLException;

public interface IPurgeToolDao {
	public void resetDatabase(String sqlScript, boolean isNew) throws SQLException;
}
