package org.masonord.config;

import org.masonord.exception.PropertyNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyConfig {
    private final Properties properties = new Properties();

    public PropertyConfig(String fileName) {
        loadPropertyFile(fileName);
    }

    public String getProperty(String property) {
        String value = properties.getProperty(property);

        if (!Objects.isNull(value)) {
            return value;
        }

        throw new PropertyNotFoundException("Property is not found: " + property);
    }

    private void loadPropertyFile(String fileName) {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input != null) {
                properties.load(input);
            }
        }catch (IOException e) {
            // TODO: do a proper logging
        }
    }

}
