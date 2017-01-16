package org.flowninja.collectord;

import org.springframework.boot.SpringApplication;

public class FlowninjaCollectord {

    public static void main(final String[] args) {
        new SpringApplication(FlowninjaConfiguration.class).run(args);
    }
}
