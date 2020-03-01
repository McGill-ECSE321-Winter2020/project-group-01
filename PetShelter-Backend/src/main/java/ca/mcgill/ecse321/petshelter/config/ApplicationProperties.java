package ca.mcgill.ecse321.petshelter.config;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Helper class that retrieves the data from application.properties.
 * @author louis
 *
 */
@Component
@PropertySource("classpath:application.properties")
public class ApplicationProperties {

    @Autowired
    private Environment env;

    public String getConfigValue(String configKey){
        return env.getProperty(configKey);
    }
}
