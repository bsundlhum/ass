package com.codeassessment.ledgerassement.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@SuppressWarnings("unused")
public class LedgerAPI {
    private List<Ledger> ledgers;
    private Integer totalPages;
    private Long totalElements;

    public LedgerAPI() {
        super();
    }

}
