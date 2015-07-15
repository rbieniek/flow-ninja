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
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class DatabaseBackedAcceptOnceFileListFilter implements FileListFilter<File>, InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseBackedAcceptOnceFileListFilter.class);
	
	static final int MAX_CHUNK_SIZE = 32;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<File> filterFiles(File[] files) {
		List<File> passingFiles = new LinkedList<File>();
		
		for(int startIndex=0; startIndex<files.length; startIndex+=MAX_CHUNK_SIZE) {
			List<File> checkedFiles = new LinkedList<File>();
			
			for(int i=0; (i<MAX_CHUNK_SIZE && startIndex+i < files.length); i++)
				checkedFiles.add(files[startIndex+i]);
			
			logger.info("checking for {} files starting from offset {}", checkedFiles.size(), startIndex);
			
			List<String> knownFileNames = jdbcTemplate.execute(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					StringBuilder queryBuilder = new StringBuilder("select fname from source_files where");
					
					for(int j=0; j<checkedFiles.size(); j++) {
						queryBuilder.append(" fname=?");
						
						if(j < (checkedFiles.size() -1))
							queryBuilder.append(" or");
					}

					return con.prepareStatement(queryBuilder.toString());
				}
			}, new PreparedStatementCallback<List<String>>() {

				@Override
				public List<String> doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
					List<String> result = new LinkedList<String>();
					
					for(int index=0; index<checkedFiles.size(); index++)
						ps.setString(index+1, checkedFiles.get(index).getName());
					
					ResultSet rs = null;

					try {
						rs = ps.executeQuery();
					
						while(rs.next()) {
							result.add(rs.getString(1));
						}
					} finally {
						if(rs != null)
							rs.close();
					}	
					
					return result;
				}
			});
			
			logger.info("already known file names: {}", knownFileNames.size());
			
			checkedFiles.stream().filter(f -> !knownFileNames.contains(f.getName())).forEach(f -> passingFiles.add(f));
		}

		SimpleJdbcInsert insert = (new SimpleJdbcInsert(jdbcTemplate)).withTableName("source_files");
		
		logger.info("adding {} files to list of already known files", passingFiles.size());
		
		for(File passingFile : passingFiles) {
			logger.info("adding file {} to list of currently processed files", passingFile);
			
			insert.execute(new MapSqlParameterSource("fname", passingFile.getName()));
		}
			
		return passingFiles;
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
