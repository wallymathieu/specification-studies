package se.gewalli.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import se.gewalli.AppendBatch;
import se.gewalli.CommandsHandler;
import se.gewalli.json.AppendToFile;
import java.util.concurrent.Executors;

@Configuration
public class AppConfig {
    @Autowired
    private Environment env;

    @Bean
    @Scope( ConfigurableBeanFactory.SCOPE_SINGLETON)
    public AppendBatch appendBatch() {
        var dbLocation = env.getProperty("FILE_DB_LOCATION");
        var logger = LoggerFactory.getLogger(AppConfig.class);
        if (dbLocation == null || dbLocation.isEmpty()) {
            logger.info("No database location found, using tmp");
            dbLocation = "/tmp/test.db";
        }
        return new AppendToFile(dbLocation,
                Executors.newFixedThreadPool(1),
                err -> logger.error("Error while appending ", err));
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST)
    public CommandsHandler persistCommandsHandler() {
        return new CommandsHandler();
    }
    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }
}