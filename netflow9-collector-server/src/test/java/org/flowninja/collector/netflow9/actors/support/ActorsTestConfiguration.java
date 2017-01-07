package org.flowninja.collector.netflow9.actors.support;

import org.springframework.context.annotation.ComponentScan;

import org.flowninja.common.TestConfig;

@TestConfig
@ComponentScan(basePackageClasses = ActorsTestConfiguration.class)
public class ActorsTestConfiguration {

}
