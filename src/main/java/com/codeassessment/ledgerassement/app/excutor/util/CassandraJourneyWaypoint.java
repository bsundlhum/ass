package com.codeassessment.ledgerassement.app.excutor.util;

import lombok.Getter;

@Getter
@SuppressWarnings("WeakerAccess")
public class CassandraJourneyWaypoint {

    private final String statement;

    public CassandraJourneyWaypoint(final String statement) {
        super();
        this.statement = statement;
    }

    public Integer getHashValue() {
        return this.statement.hashCode();
    }
}
