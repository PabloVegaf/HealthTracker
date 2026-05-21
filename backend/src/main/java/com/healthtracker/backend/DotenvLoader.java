package com.healthtracker.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Utilidad para cargar variables de entorno desde un archivo .env local.
 */
public final class DotenvLoader {

    private DotenvLoader() {}

    public static void load() {
        Path path = Paths.get(System.getProperty("user.dir")).resolve("../.env");

        if (!Files.exists(path)) {
            path = Paths.get(System.getProperty("user.dir")).resolve(".env");
        }

        if (!Files.exists(path)) {
            return;
        }

        Properties props = new Properties();
        try (var reader = Files.newBufferedReader(path)) {
            props.load(reader);
        } catch (IOException e) {
            return;
        }

        for (String key : props.stringPropertyNames()) {
            if (System.getProperty(key) == null) {
                System.setProperty(key, props.getProperty(key));
            }
        }
    }
}
