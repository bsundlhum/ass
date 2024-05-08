package com.codeassessment.ledgerassement.domain.model;

import com.codeassessment.ledgerassement.config.ApplicationName;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;

import javax.sql.DataSource;

public class FlywayFactoryBean {

    private final ApplicationName applicationName;

    public FlywayFactoryBean(final ApplicationName applicationName) {
        super();
        this.applicationName = applicationName;
    }

    public Flyway create(final DataSource dataSource) {

        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("db/migrations/mariadb");
        flyway.setTable(this.applicationName.getServiceName() + "_schema_version");
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0");
        return flyway;
    }
}
