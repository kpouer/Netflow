package com.kpouer.netflow.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class NetflowOptions {
    private final Properties options = new Properties();

    public NetflowOptions() throws IOException {
        Path path = Paths.get("netflow.properties");
        if (Files.exists(path)) {
            try (var inStream = Files.newInputStream(path)) {
                options.load(inStream);
            }
        }
    }

    public boolean isTrue(String key) {
        String property = options.getProperty(key);
        return "true".equalsIgnoreCase(property);
    }
}
