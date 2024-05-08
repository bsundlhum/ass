package com.codeassessment.ledgerassement.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SuppressWarnings("WeakerAccess")
public class TrialBalanceEntry {

    private Ledger ledger;
    private Type type;
    private Double amount;

    public TrialBalanceEntry() {
        super();
    }

    public enum Type {
        DEBIT,
        CREDIT
    }
}
