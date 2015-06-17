/**
 * 
 */
package org.flowninja.collector.client;

import org.flowninja.collector.client.components.impl.rest.ClientDetailsRestController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author rainer
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackageClasses=ClientDetailsRestController.class)
public class DispatcherServletConfiguration extends WebMvcConfigurerAdapter {

}
