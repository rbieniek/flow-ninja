package org.flowninja.collector.netflow9;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.flowninja.collector.netflow9.components.InetSocketAddressPeerAddressMapper;
import org.flowninja.collector.netflow9.components.PeerAddressMapper;

@Configuration
@ComponentScan(basePackageClasses = Netflow9CollectorConfiguration.class)
@EnableAutoConfiguration(
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class })
@EnableConfigurationProperties
public class Netflow9CollectorConfiguration {

    @Bean
    public PeerAddressMapper peerAddressMapper() {
        return new InetSocketAddressPeerAddressMapper();
    }
}
