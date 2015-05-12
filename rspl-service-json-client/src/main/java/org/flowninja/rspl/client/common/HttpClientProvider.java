/**
 * 
 */
package org.flowninja.rspl.client.common;

import java.io.IOError;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class HttpClientProvider implements InitializingBean, DisposableBean,FactoryBean<CloseableHttpClient> {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientProvider.class);

	private PoolingHttpClientConnectionManager connectionManager;
	
	@Override
	public void afterPropertiesSet() throws Exception {		
		logger.info("starting HTTP client provider");

		connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(200);
	}

	@Override
	public void destroy() throws Exception {
		logger.info("stopping HTTP client provider");
		
		try {
		connectionManager.close();
		} catch(IOError e) {
			logger.error("problems closing HTTP client connection manager", e);
		}
	}

	@Override
	public CloseableHttpClient getObject() throws Exception {
		return HttpClientBuilder.create()
				.setConnectionManager(connectionManager)
				.setConnectionManagerShared(true).build();
	}

	@Override
	public Class<?> getObjectType() {
		return CloseableHttpClient.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

}
