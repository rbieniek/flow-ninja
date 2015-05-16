/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import static org.fest.assertions.api.Assertions.*;

import java.io.File;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author rainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=IntegrationTestConfig.class)
public class IgnoreCurrentHourFileFilterTest {

	@Autowired
	private IgnoreCurrentHourFileFilter filter;
	
	@Test
	public void ignoreCurrentHourFiles() {
		assertThat(filter.filterFiles(new File[] {
				new File("data-flow-" + timestamp() + ".json"),
				new File("options-flow-" + timestamp() + ".json"),
		})).isEmpty();
	}
	
	@Test
	public void passPreviousHourFiles() {
		assertThat(filter.filterFiles(new File[] {
				new File("data-flow-" + timestamp(System.currentTimeMillis() - 3600L*1000L) + ".json"),
				new File("options-flow-" + timestamp(System.currentTimeMillis() - 3600L*1000L) + ".json"),
		})).hasSize(2);
	}
	
	@Test
	public void ignorePreviousHourFilesWithoutSuffix() {
		assertThat(filter.filterFiles(new File[] {
				new File("data-flow-" + timestamp(System.currentTimeMillis() - 3600L*1000L)),
				new File("options-flow-" + timestamp(System.currentTimeMillis() - 3600L*1000L)),
		})).isEmpty();;
	}
	
	@Test
	public void ignorePreviousHourFilesWithoutPrefix() {
		assertThat(filter.filterFiles(new File[] {
				new File("data-flo" + timestamp(System.currentTimeMillis() - 3600L*1000L) + ".json"),
				new File("options-flo" + timestamp(System.currentTimeMillis() - 3600L*1000L) + ".json"),
		})).isEmpty();;
	}
	
	@SuppressWarnings("deprecation")
	private String timestamp(long stamp) {
		Date now = new Date(stamp);
		
		return String.format("%04d%02d%02d-%02d", now.getYear() + 1900, now.getMonth()+1, now.getDate(), now.getHours());
	}
	
	private String timestamp() {
		return timestamp(System.currentTimeMillis());
	}
}
