/**
 * 
 */
package org.flowninja.collector.client;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author rainer
 *
 */
@Configuration
public class TestConfiguration {

	@Bean
	public HttpClientConnectionManager connectionManager() {
			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
			
			return cm;
	}
	
	@Bean
	public FactoryBean<CloseableHttpClient> httpClient() {
		return new FactoryBean<CloseableHttpClient>() {

			@Override
			public CloseableHttpClient getObject() throws Exception {
				return HttpClients.custom()
						.setConnectionManager(connectionManager())
						.build();
			}

			@Override
			public Class<?> getObjectType() {
				return CloseableHttpClient.class;
			}

			@Override
			public boolean isSingleton() {
				return false;
			}
		};
		
	}
	
	@Bean
	public Server httpServer() {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		
		server.setConnectors(new Connector[] { connector });
		
		ServletContextHandler context = new ServletContextHandler();

		context.setContextPath("/");

		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();

		appContext.register(OAuth2AuthorizationServerConfiguration.class,
				OAuth2ResourceServerConfiguration.class,
				WebSecurityConfiguration.class,
				DispatcherServletConfiguration.class);
				
		context.addEventListener(new ContextLoaderListener(appContext));
		context.addEventListener(new RequestContextListener());
		context.addFilter(new FilterHolder(new DelegatingFilterProxy("springSecurityFilterChain", appContext)), "/*", EnumSet.of(DispatcherType.REQUEST));
		context.addServlet(new ServletHolder(new DispatcherServlet(appContext)), "/*");

		server.setHandler(context);
		
		return server;
	}
	
	@Bean
	public ServerConnector serverConnector() {
		Server server = httpServer();
		
		return (ServerConnector)server.getConnectors()[0];
	}
}
