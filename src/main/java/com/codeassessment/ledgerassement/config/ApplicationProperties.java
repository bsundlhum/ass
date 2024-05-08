package com.codeassessment.ledgerassement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.reactive.config.PathMatchConfigurer;

@Configuration
@EnableDiscoveryClient
@EnableAsync
@EnableTenantContext
@EnableMariaDB
@EnableCassandra
@EnableCommandProcessing
@EnableAnubis
@EnableServiceException
@ComponentScan({
        "com.codeassessment.ledgerassement.app.excutor.rest",
        "com.codeassessment.ledgerassement.app.excutor.service"
})
public class ApplicationProperties {
    public ApplicationProperties() {super();}

//    @Bean(name = ServiceConstants.LOGGER_NAME)
//    public Logger logger() {
//        return LoggerFactory.getLogger(ServiceConstants.LOGGER_NAME);
//    }

}
