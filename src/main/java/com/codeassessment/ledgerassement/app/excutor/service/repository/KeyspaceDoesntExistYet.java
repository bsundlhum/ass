package com.codeassessment.ledgerassement.app.excutor.service.repository;

import com.datastax.driver.core.exceptions.InvalidQueryException;

class KeyspaceDoesntExistYet extends IllegalArgumentException {
    KeyspaceDoesntExistYet(String s, InvalidQueryException ex) {
        super(s, ex);
    }
}
