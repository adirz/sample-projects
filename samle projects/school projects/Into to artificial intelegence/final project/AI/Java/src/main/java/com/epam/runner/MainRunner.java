package com.epam.runner;

import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URISyntaxException;

import static java.lang.System.setProperty;
import static org.slf4j.bridge.SLF4JBridgeHandler.install;
import static org.slf4j.bridge.SLF4JBridgeHandler.removeHandlersForRootLogger;

/**
 * This is a start point for runner-java.
 *
 * @author uladzimir_zhuraulevich@epam.com
 */
public class MainRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MainRunner.class);

    public static void main(String[] args) {
        try {
            // to disable logging for org.restlet
            // see http://restlet.com/technical-resources/restlet-framework/guide/2.2/editions/jse/logging
            setProperty("java.util.logging.config.file", LOG.getClass().getClassLoader().getResource("logging.properties").toURI().toString());

            // Optionally remove existing handlers attached to j.u.l root logger
            removeHandlersForRootLogger();  // (since SLF4J 1.6.5)
            // add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
            // the initialization phase of your application
            install();
        } catch (URISyntaxException e) {
            // do nothing
        }

        LOG.info(">> Runner-Java is starting...");
        // starting camel context via spring
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext("com/epam/spring-main.xml");
        final ProducerTemplate producerTemplate = applicationContext.getBean(ProducerTemplate.class);

        if (producerTemplate != null) {
            // sending request to Game Server that Runner-Java is ready
            LOG.info(">> Response from GAME SERVER: {}", producerTemplate.requestBody(null));
        }

    }
}
