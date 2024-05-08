package com.codeassessment.ledgerassement.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@SuppressWarnings("unused")
public class TrialBalance {

    private List<TrialBalanceEntry> trialBalanceEntries;
    private Double debitTotal;
    private Double creditTotal;

    public TrialBalance() {
        super();
    }
}
