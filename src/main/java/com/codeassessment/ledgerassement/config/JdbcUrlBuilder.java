package com.codeassessment.ledgerassement.config;

public final class JdbcUrlBuilder {

    private final DatabaseType type;
    private String host;
    private String port;
    private String instanceName;

    private JdbcUrlBuilder(final DatabaseType type) {
        super();
        this.type = type;
    }

    public static JdbcUrlBuilder create(final DatabaseType type) {
        return new JdbcUrlBuilder(type);
    }

    public JdbcUrlBuilder host(final String host) {
        this.host = host;
        return this;
    }

    public JdbcUrlBuilder port(final String port) {
        this.port = port;
        return this;
    }

    public JdbcUrlBuilder instanceName(final String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public String build() {
        final String[] hostList = this.host.split(",");
        switch (this.type) {
            case MARIADB:
                final StringBuilder jdbcUrl = new StringBuilder();
                final String jdbcProtocol = this.type.prefix() + (hostList.length > 1 ? "replication://" : "//");
                jdbcUrl.append(jdbcProtocol);
                for (int i = 0; i < hostList.length; i++) {
                    jdbcUrl.append(hostList[i].trim()).append(":").append(this.port);
                    if ((i + 1) < hostList.length) {
                        jdbcUrl.append(",");
                    }
                }
                if (this.instanceName != null) {
                    jdbcUrl.append("/").append(this.instanceName);
                }
                return jdbcUrl.toString();
            default:
                throw new IllegalArgumentException("Unknown database type '" + this.type.name() + "'");
        }
    }

    public enum DatabaseType {
        MARIADB("jdbc:mariadb:");

        private final String prefix;

        DatabaseType(final String prefix) {
            this.prefix = prefix;
        }

        String prefix() {
            return this.prefix;
        }
    }
}
