package org.flowninja.collectord.components;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.beans.factory.InitializingBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileSinkManager implements InitializingBean {

    private final FileSinkProperties fileSinkProperties;

    private File baseDirectory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.baseDirectory = new File(fileSinkProperties.getBaseDirectory());

        if (!baseDirectory.isDirectory()) {
            if (!this.baseDirectory.mkdirs()) {
                throw new IllegalStateException("Cannot create base directory " + fileSinkProperties.getBaseDirectory());
            }
        }
    }

    public File createTargetFile(final String lastPartName) {
        final String intermediatePart = String.format(fileSinkProperties.getIntermediatePattern(), LocalDateTime.now());
        final File targetDirectory = new File(baseDirectory, intermediatePart);

        if (!targetDirectory.isDirectory()) {
            if (!targetDirectory.mkdirs()) {
                throw new IllegalStateException("Cannot create target directory " + targetDirectory.getAbsolutePath());
            }
        }

        return new File(targetDirectory, lastPartName);
    }
}
