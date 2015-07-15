/**
 * 
 */
package org.flowninja.shell.transferrer.data;

import javax.sql.DataSource;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class JdbcTemplateFactory implements FactoryBean<JdbcTemplate> {

	@Autowired
	private DataSource dataSource;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public JdbcTemplate getObject() throws Exception {
		return new JdbcTemplate(dataSource);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return JdbcTemplate.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return false;
	}

}
