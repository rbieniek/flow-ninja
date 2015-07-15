/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=IntegrationTestConfig.class)
@DirtiesContext(classMode=ClassMode.AFTER_EACH_TEST_METHOD)
public class DatabaseBackedAcceptOnceFileListFilterTest {
	@Configuration
	public static class TestConfiguration {
		@Bean
		public DataSource embeddedDataSource() {
			return (new EmbeddedDatabaseBuilder()).setType(EmbeddedDatabaseType.DERBY).addScript("accept-once-create.sql").build();
		}
		
		@Bean
		public JdbcTemplate jdbcTemplate() {
			return new JdbcTemplate(embeddedDataSource());
		}

		@Bean
		public PlatformTransactionManager transactionManager() {
			return new DataSourceTransactionManager(embeddedDataSource());
		}
	}

	@Autowired
	private DatabaseBackedAcceptOnceFileListFilter filter;
	
	@Test
	public void singleFilePassing() {
		File file = new File("file-" + UUID.randomUUID().toString());
		
		List<File> passingFiles = filter.filterFiles(new File[] {file});
		
		assertThat(passingFiles).contains(file);
	}
	
	@Test
	public void singleFileRepeat() {
		File file = new File("file-" + UUID.randomUUID().toString());
		List<File> passingFiles;
		
		passingFiles = filter.filterFiles(new File[] {file});
		assertThat(passingFiles).contains(file);
		
		passingFiles = filter.filterFiles(new File[] {file});
		assertThat(passingFiles).isEmpty();
	}

	@Test
	public void twoFilesPassing() {
		File fileOne = new File("file-" + UUID.randomUUID().toString());
		File fileTwo = new File("file-" + UUID.randomUUID().toString());
		
		List<File> passingFiles = filter.filterFiles(new File[] {fileOne, fileTwo});
		
		assertThat(passingFiles).contains(fileOne, fileTwo);
	}
	
	@Test
	public void twoFilesRepeat() {
		File fileOne = new File("file-" + UUID.randomUUID().toString());
		File fileTwo = new File("file-" + UUID.randomUUID().toString());
		
		List<File> passingFiles = filter.filterFiles(new File[] {fileOne, fileTwo});
		
		assertThat(passingFiles).contains(fileOne, fileTwo);
		
		passingFiles = filter.filterFiles(new File[] {fileOne, fileTwo});
		assertThat(passingFiles).isEmpty();
	}


	@Test
	public void oneHundredFilePassing() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		
		for(int i=0; i<100; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		List<File> passingFiles = filter.filterFiles(checkedFiles.toArray(new File[0]));
		
		assertThat(passingFiles).contains(checkedFiles.toArray(new File[0]));
	}
	
	@Test
	public void oneHundredFileRepeat() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		List<File> passingFiles;

		for(int i=0; i<100; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		File[] files = checkedFiles.toArray(new File[0]);

		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).contains(files);
		
		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).isEmpty();
	}

	@Test
	public void oneThousandFilePassing() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		
		for(int i=0; i<1000; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		List<File> passingFiles = filter.filterFiles(checkedFiles.toArray(new File[0]));
		
		assertThat(passingFiles).contains(checkedFiles.toArray(new File[0]));
	}
	
	@Test
	public void oneThousandFileRepeat() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		List<File> passingFiles;

		for(int i=0; i<1000; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		File[] files = checkedFiles.toArray(new File[0]);

		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).contains(files);
		
		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).isEmpty();
	}

	@Test
	public void oneChunkFilePassing() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		
		for(int i=0; i<DatabaseBackedAcceptOnceFileListFilter.MAX_CHUNK_SIZE; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		List<File> passingFiles = filter.filterFiles(checkedFiles.toArray(new File[0]));
		
		assertThat(passingFiles).contains(checkedFiles.toArray(new File[0]));
	}
	
	@Test
	public void oneChunkFileRepeat() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		List<File> passingFiles;

		for(int i=0; i<DatabaseBackedAcceptOnceFileListFilter.MAX_CHUNK_SIZE; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		File[] files = checkedFiles.toArray(new File[0]);

		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).contains(files);
		
		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).isEmpty();
	}

	@Test
	public void twoChunkFilePassing() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		
		for(int i=0; i<DatabaseBackedAcceptOnceFileListFilter.MAX_CHUNK_SIZE; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		List<File> passingFiles = filter.filterFiles(checkedFiles.toArray(new File[0]));
		
		assertThat(passingFiles).contains(checkedFiles.toArray(new File[0]));
	}
	
	@Test
	public void twoChunkFileRepeat() {
		ArrayList<File> checkedFiles = new ArrayList<File>();
		List<File> passingFiles;

		for(int i=0; i<DatabaseBackedAcceptOnceFileListFilter.MAX_CHUNK_SIZE; i++)
			checkedFiles.add(new File("file-" + UUID.randomUUID().toString()));
		
		File[] files = checkedFiles.toArray(new File[0]);

		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).contains(files);
		
		passingFiles = filter.filterFiles(files);
		assertThat(passingFiles).isEmpty();
	}
}
