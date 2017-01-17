package org.flowninja.collectord;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.flowninja.collector.common.netflow9.components.Netflow9CommonComponentsConfiguration;
import org.flowninja.collector.netflow9.Netflow9CollectorConfiguration;
import org.flowninja.collectord.actors.FlowninjaActorsConfiguration;
import org.flowninja.collectord.components.FlowninjaComponentsConfiguration;
import org.flowninja.common.akka.AkkaConfiguration;

@Configuration
@Import({
        AkkaConfiguration.class,
        Netflow9CollectorConfiguration.class,
        FlowninjaActorsConfiguration.class,
        FlowninjaComponentsConfiguration.class,
        Netflow9CommonComponentsConfiguration.class })
@EnableConfigurationProperties
public class FlowninjaConfiguration {

    @Configuration
    @EnableWebSecurity
    public class FormLoginSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        public void configure(final WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/flows");
        }

        @Override
        protected void configure(final HttpSecurity http) throws Exception {
            http.authorizeRequests().antMatchers("/flows").anonymous();
        }
    }
}
