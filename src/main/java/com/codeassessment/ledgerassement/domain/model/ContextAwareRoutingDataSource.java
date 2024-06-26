package com.codeassessment.ledgerassement.domain.model;

import com.jolbox.bonecp.BoneCPDataSource;
import com.codeassessment.ledgerassement.app.excutor.util.TenantContextHolder;
import com.codeassessment.ledgerassement.config.JdbcUrlBuilder;
import com.codeassessment.ledgerassement.config.MariaDBConstants;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public final class ContextAwareRoutingDataSource extends AbstractRoutingDataSource {

    private final Logger logger;
    private final JdbcUrlBuilder.DatabaseType type;
    private final ConcurrentHashMap<String, DataSource> dynamicDataSources;
    private DataSource metaDataSource;

    public ContextAwareRoutingDataSource(@Qualifier(MariaDBConstants.LOGGER_NAME) final Logger logger,
                                         final JdbcUrlBuilder.DatabaseType type) {
        super();
        this.logger = logger;
        this.type = type;
        this.dynamicDataSources = new ConcurrentHashMap<>();
    }

    public void setMetaDataSource(final DataSource metaDataSource) {
        this.metaDataSource = metaDataSource;
        super.setDefaultTargetDataSource(metaDataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.checkedGetIdentifier();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        if (!TenantContextHolder.identifier().isPresent()) {
            this.logger.warn("Tenant context not available.");
            return super.determineTargetDataSource();
        }

        final String currentLookupKey = this.determineCurrentLookupKey().toString();

        this.dynamicDataSources.computeIfAbsent(currentLookupKey, (key) -> {
            this.logger.info("Creating new dynamic data source for {}.", key);
            final DBTenant tenant = new DBTenant(key);
            this.readAdditionalTenantInformation(tenant);
            final BoneCPDataSource tenantDataSource = new BoneCPDataSource();
            tenantDataSource.setDriverClass(tenant.getDriverClass());
            tenantDataSource.setJdbcUrl(JdbcUrlBuilder
                    .create(this.type)
                    .host(tenant.getHost())
                    .port(tenant.getPort())
                    .instanceName(tenant.getDatabaseName())
                    .build());
            tenantDataSource.setUsername(tenant.getUser());
            tenantDataSource.setPassword(tenant.getPassword());

            final BoneCPDataSource boneCpMetaDataSource = (BoneCPDataSource) this.metaDataSource;
            tenantDataSource.setIdleConnectionTestPeriodInMinutes(boneCpMetaDataSource.getIdleConnectionTestPeriodInMinutes());
            tenantDataSource.setIdleMaxAgeInMinutes(boneCpMetaDataSource.getIdleMaxAgeInMinutes());
            tenantDataSource.setMaxConnectionsPerPartition(boneCpMetaDataSource.getMaxConnectionsPerPartition());
            tenantDataSource.setMinConnectionsPerPartition(boneCpMetaDataSource.getMinConnectionsPerPartition());
            tenantDataSource.setPartitionCount(boneCpMetaDataSource.getPartitionCount());
            tenantDataSource.setAcquireIncrement(boneCpMetaDataSource.getAcquireIncrement());
            tenantDataSource.setStatementsCacheSize(boneCpMetaDataSource.getStatementsCacheSize());
            return tenantDataSource;
        });

        return this.dynamicDataSources.get(currentLookupKey);
    }

    private void readAdditionalTenantInformation(final DBTenant tenant) {
        this.logger.info("Reading additional information for {}.", tenant.getIdentifier());
        @SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"}) final String query = "SELECT driver_class, database_name, host, port, a_user, pwd FROM seshat.tenants WHERE identifier = ?";
        try (final Connection connection = this.metaDataSource.getConnection()) {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, tenant.getIdentifier());
                final ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    tenant.setDriverClass(resultSet.getString("driver_class"));
                    tenant.setDatabaseName(resultSet.getString("database_name"));
                    tenant.setHost(resultSet.getString("host"));
                    tenant.setPort(resultSet.getString("port"));
                    tenant.setUser(resultSet.getString("a_user"));
                    tenant.setPassword(resultSet.getString("pwd"));
                }
            }
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Could not fetch information for tenant '" + tenant.getIdentifier() + "'", ex);
        }
    }
}
