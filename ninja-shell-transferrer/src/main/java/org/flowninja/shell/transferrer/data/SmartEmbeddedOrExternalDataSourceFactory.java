/**
 * 
 */
package org.flowninja.shell.transferrer.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class SmartEmbeddedOrExternalDataSourceFactory implements FactoryBean<DataSource>, InitializingBean, DisposableBean {
	private static final Logger logger = LoggerFactory.getLogger(SmartEmbeddedOrExternalDataSourceFactory.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ResourceLoader resourceLoader;

	
	private File embeddedDatabaseDirectory;
	private String connectURI;
	private String userName;
	private String password;
	
	private PoolingDataSource<PoolableConnection> dataSource;
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	@Override
	public DataSource getObject() throws Exception {
		return dataSource;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Properties props = new Properties();
		boolean createDb = false;
		
		this.connectURI = env.getProperty("dbConnectURI");
		this.userName = env.getProperty("dbUserName");
		this.password = env.getProperty("dbPassword");
		
		if(env.getProperty("embeddedDatabaseDirectory") != null) {

			this.embeddedDatabaseDirectory= new File(env.getProperty("embeddedDatabaseDirectory"));
			
			if(!this.embeddedDatabaseDirectory.isDirectory()) {
				logger.info("database will be created at location {}", this.embeddedDatabaseDirectory);
				
				createDb = true;
			}
	
			StringBuilder uriBuilder = new StringBuilder("jdbc:derby:directory:");
			
			uriBuilder.append(this.embeddedDatabaseDirectory.getAbsolutePath());
			if(createDb)
				uriBuilder.append(";create=true");
			
			this.connectURI = uriBuilder.toString();
			
			logger.info("usinged embedded database connect URI {}", connectURI);
			
			// clear user name and password since not supported by embedded database
			this.userName = null;
			this.password = null;

//			if(createDb) {
//				Connection con = DriverManager.getConnection(connectURI);
//				Statement stmt = con.createStatement();
//				
//				stmt.execute("create table source_files (fname varchar(256) primary key)");
//				
//				stmt.close();
//				con.close();
//			}
		}
		
		if(StringUtils.isBlank(this.connectURI))
			throw new IllegalArgumentException("Either emebedded database work directory (-DembeddedDatabaseDirectory=) or databsae connect URI (-DdbConnectURI=) must be specified");
		
		if(StringUtils.isNotBlank(userName)) {
			props.setProperty("user", userName);
		}
		if(StringUtils.isNotBlank(password)) {
			props.setProperty("password", password);
		}
		
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, props);
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnectionFactory);
		
		poolableConnectionFactory.setPool(connectionPool);
		
		dataSource = new PoolingDataSource<PoolableConnection>(connectionPool);
		
		if(createDb) {
			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			
			populator.addScript(resourceLoader.getResource("classpath:/org/springframework/integration/jdbc/schema-derby.sql"));
			populator.addScript(resourceLoader.getResource("classpath:/org/flowninja/shell/transferrer/integration/schema-derby.sql"));
			
			populator.execute(dataSource);
		}
	}

	@Override
	public void destroy() throws Exception {
		if(this.embeddedDatabaseDirectory != null && dataSource != null) {
			DriverManager.getConnection("jdbc:derby:;shutdown=true");
		}
	}

}
