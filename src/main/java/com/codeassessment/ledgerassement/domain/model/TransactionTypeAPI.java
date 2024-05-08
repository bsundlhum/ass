package com.codeassessment.ledgerassement.domain.model;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@SuppressWarnings("unused")
public class TransactionTypeAPI {
    @Valid
    private List<TransactionType> transactionTypes;
    private Integer totalPages;
    private Long totalElements;

    public TransactionTypeAPI() {
        super();
    }

    public void add(final TransactionType transactionType) {
        if (this.transactionTypes == null) {
            this.transactionTypes = new ArrayList<>();
        }

        this.transactionTypes.add(transactionType);
    }
}
