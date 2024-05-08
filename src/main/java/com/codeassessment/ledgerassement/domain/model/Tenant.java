package com.codeassessment.ledgerassement.domain.model;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table(keyspace = "assessledger", name = "tenants")
public final class Tenant {

    @PartitionKey
    private String identifier;

    @Column(name = "cluster_name")
    private String clusterName;

    @Column(name = "contact_points")
    private String contactPoints;

    @Column(name = "keyspace_name")
    private String keyspace;

    public Tenant() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tenant that = (Tenant) o;

        return identifier.equals(that.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }
}
