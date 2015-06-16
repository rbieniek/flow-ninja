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
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
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
	public CloseableHttpClient httpClient() {
		return HttpClients.custom()
				.setConnectionManager(connectionManager())
				.build();
	}
	
	@Bean
	public Server httpServer() {
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		
		server.setConnectors(new Connector[] { connector });
		
		ServletContextHandler context = new ServletContextHandler();

		context.setContextPath("/");
		
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		AnnotationConfigWebApplicationContext dispatcherServletContext = new AnnotationConfigWebApplicationContext();

		rootContext.register(RootConfiguration.class);
		dispatcherServletContext.register(DispatcherServletConfiguration.class);		
		dispatcherServletContext.setParent(rootContext);
		
		rootContext.refresh();
		dispatcherServletContext.refresh();
		
		context.addEventListener(new ContextLoaderListener(dispatcherServletContext));
		context.addEventListener(new RequestContextListener());
		context.addFilter(DelegatingFilterProxy.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		context.addServlet(new ServletHolder(new DispatcherServlet(dispatcherServletContext)), "/*");
		
		server.setHandler(context);
		
		return server;
	}
	
	@Bean
	public ServerConnector serverConnector() {
		Server server = httpServer();
		
		return (ServerConnector)server.getConnectors()[0];
	}
}
