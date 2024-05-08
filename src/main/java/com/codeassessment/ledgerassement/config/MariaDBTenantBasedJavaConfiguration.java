package com.codeassessment.ledgerassement.config;

import com.codeassessment.ledgerassement.domain.model.ContextAwareRoutingDataSource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@SuppressWarnings("WeakerAccess")
@Configuration
@ConditionalOnProperty(prefix = "mariadb", name = "enabled", matchIfMissing = true)
public class MariaDBTenantBasedJavaConfiguration {
    @Bean
    public DataSource dataSource(@Qualifier(MariaDBConstants.LOGGER_NAME) final Logger logger,
                                 final MetaDataSourceWrapper metaDataSource) {

        final ContextAwareRoutingDataSource dataSource = new ContextAwareRoutingDataSource(logger, JdbcUrlBuilder.DatabaseType.MARIADB);
        dataSource.setMetaDataSource(metaDataSource.getMetaDataSource());
        final HashMap<Object, Object> targetDataSources = new HashMap<>();
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }
}