package net.bndy.config;

import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ConfigurationManager {

    public static <T extends FileBasedConfiguration> FileBasedConfigurationBuilder<T> getConfigurationBuilder(String filename) {
        checkConfigurationFile(filename);

        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = null;

        if (filename.toLowerCase().endsWith(".properties")) {
            configurationBuilder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class);
            configurationBuilder.configure(params.properties().setFileName(filename));
        } else if (filename.toLowerCase().endsWith(".yml")) {
            configurationBuilder = new FileBasedConfigurationBuilder<>(YAMLConfiguration.class);
            configurationBuilder.configure(params.hierarchical().setFileName(filename));
        } else if (filename.toLowerCase().endsWith(".xml")) {
            configurationBuilder = new FileBasedConfigurationBuilder<>(XMLConfiguration.class);
            configurationBuilder.configure(params.xml().setFileName(filename));
        }
        return (FileBasedConfigurationBuilder<T>) configurationBuilder;
    }

    public static Integer getSize(String configurationFilename) {
        checkConfigurationFile(configurationFilename);

        FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = getConfigurationBuilder(configurationFilename);
        FileBasedConfiguration configuration = null;
        try {
            configuration = configurationBuilder.getConfiguration();
            return configuration.size();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getConfiguration(String configurationFilename, String key, Class<T> cls, T defaultValue) {
        checkConfigurationFile(configurationFilename);

        FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = getConfigurationBuilder(configurationFilename);
        FileBasedConfiguration configuration = null;
        try {
            configuration = configurationBuilder.getConfiguration();
            if (configuration != null) {
                T value = configuration.get(cls, key);
                if (value == null) {
                    return defaultValue;
                }

                return value;
            }
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getConfiguration(String configurationFilename, String key, Class<T> cls) {
        return getConfiguration(configurationFilename, key, cls, null);
    }

    public static Iterator<String> getKeys(String configurationFilename) {
        return getKeys(configurationFilename, null);
    }

    public static Iterator<String> getKeys(String configurationFilename, String prefix) {
        checkConfigurationFile(configurationFilename);

        FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = getConfigurationBuilder(configurationFilename);
        FileBasedConfiguration configuration = null;
        try {
            configuration = configurationBuilder.getConfiguration();
            if (prefix == null || prefix.isEmpty()) {
                return configuration.getKeys();
            }
            return configuration.getKeys(prefix);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveConfiguration(String configurationFilename, String key, Object value) {
        operateConfiguration(Action.SAVE, configurationFilename, key, value);
    }

    public static void removeConfiguration(String configurationFilename, String key) {
        operateConfiguration(Action.REMOVE, configurationFilename, key, null);
    }

    private static void operateConfiguration(Action action, String configurationFilename, String key, Object value) {
        checkConfigurationFile(configurationFilename);

        FileBasedConfigurationBuilder<FileBasedConfiguration> configurationBuilder = getConfigurationBuilder(configurationFilename);
        FileBasedConfiguration configuration = null;
        try {
            configuration = configurationBuilder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        boolean alreadyExisted = configuration.containsKey(key);
        switch (action) {
            case SAVE:
                if (alreadyExisted) {
                    configuration.setProperty(key, value);
                } else {
                    configuration.addProperty(key, value);
                }
                break;

            case REMOVE:
                if (alreadyExisted) {
                    configuration.clearProperty(key);
                }
                break;
        }

        try {
            configurationBuilder.save();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void checkConfigurationFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            throw new Error("The file name MUST not be empty.");
        }
    }

    protected enum Action {
        SAVE,
        REMOVE,
    }
}
