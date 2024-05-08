package com.codeassessment.ledgerassement.domain.model;

import com.codeassessment.ledgerassement.config.MetaDataSourceWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@SuppressWarnings("WeakerAccess")
@Configuration
@ConditionalOnProperty(prefix = "mariadb", name = "enabled", matchIfMissing = true)
public class MariaDBTenantFreeJavaConfiguration {
    @Bean
    public DataSource dataSource(final MetaDataSourceWrapper metaDataSource) {
        return metaDataSource.getMetaDataSource();
    }
}
