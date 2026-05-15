package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {

    private static final String DEFAULT_CONFIG = "config.properties";
    private static final String LOCAL_CONFIG = "local.properties";
    private static volatile Properties properties;

    private PropertyReader() {
    }

    public static String getProperty(String propertyName) {
        String systemValue = System.getProperty(propertyName);
        if (systemValue != null) {
            return systemValue;
        }

        return loadProperties().getProperty(propertyName);
    }

    private static Properties loadProperties() {
        if (properties == null) {
            synchronized (PropertyReader.class) {
                if (properties == null) {
                    properties = readProperties();
                }
            }
        }

        return properties;
    }

    private static Properties readProperties() {
        Properties loadedProperties = new Properties();
        loadFromClasspath(loadedProperties, DEFAULT_CONFIG);
        loadOptionalFromClasspath(loadedProperties, LOCAL_CONFIG);

        String configFile = System.getProperty("config_file", loadedProperties.getProperty("config_file"));
        if (configFile != null && !configFile.isBlank()) {
            loadFromFileSystem(loadedProperties, configFile);
        }

        return loadedProperties;
    }

    private static void loadFromClasspath(Properties target, String resourceName) {
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new RuntimeException("Config resource not found in classpath: " + resourceName);
            }
            target.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read classpath config: " + resourceName, exception);
        }
    }

    private static void loadOptionalFromClasspath(Properties target, String resourceName) {
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                target.load(inputStream);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read optional classpath config: " + resourceName, exception);
        }
    }

    private static void loadFromFileSystem(Properties target, String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            target.load(inputStream);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read config file: " + path, exception);
        }
    }
}
