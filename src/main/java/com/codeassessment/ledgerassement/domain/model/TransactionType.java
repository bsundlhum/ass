package com.codeassessment.ledgerassement.domain.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TransactionType {

    private String code;
    @NotEmpty
    private String name;
    private String description;

    public TransactionType() {
        super();
    }

}
