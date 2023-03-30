package com.kpouer.netflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, String> inventory = new HashMap<>();

    public Inventory() throws IOException {
        Path inventoryPath = Path.of("inventory.txt");
        if (Files.exists(inventoryPath)) {
            Files
                .readAllLines(inventoryPath)
                .stream().map(line -> line.split("="))
                .filter(parts -> parts.length == 2)
                .forEach(parts -> inventory.put(parts[0], parts[1]));
        }
    }

    public String get(String key) {
        String result = inventory.getOrDefault(key, key);
        return result.replace("-", "_");
    }
}
