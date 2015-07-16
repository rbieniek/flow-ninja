/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class IgnoreDuplicateFileFilter implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(IgnoreDuplicateFileFilter.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public boolean canFilePass(File dataFile) {
		boolean fileKnown = jdbcTemplate.execute(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				return con.prepareStatement("select count(fname) from source_files where fname=?");
			}
		}, new PreparedStatementCallback<Boolean>() {

			@Override
			public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ResultSet rs = null;
				int count = 0;
				
				ps.setString(1, dataFile.getName());
				
				try {
					rs = ps.executeQuery();
					
					if(rs.next())
						count = rs.getInt(1);
				} finally {
					rs.close();
				}
				
				return count > 0;
			}
		});

		logger.info("data file {} is already known: {}", dataFile, fileKnown);
		
		return !fileKnown;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		jdbcTemplate.execute(new StatementCallback<Object>() {

			@Override
			public Object doInStatement(Statement stmt) throws SQLException, DataAccessException {
				stmt.execute("delete from source_files");
				
				return null;
			}
		});
	}
}
