package org.flowninja.collectord.components;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "collectord.disk")
public class FileSinkProperties {

    private String baseDirectory;
    private String intermediatePattern;
}
