package com.codeassessment.ledgerassement.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@SuppressWarnings({"unused"})
public final class Ledger {

    @NotNull
    private AccountType type;
    private String identifier;
    @NotEmpty
    private String name;
    private String description;
    private String parentLedgerIdentifier;
    @Valid
    private List<Ledger> subLedgers;
    private String createdOn;
    private String createdBy;
    private String lastModifiedOn;
    private String lastModifiedBy;
    @NotNull
    private Boolean showAccountsInChart;

    public Ledger() {
        super();
    }

    public String getType() {
        return this.type.name();
    }

    public void setType(final String type) {
        this.type = AccountType.valueOf(type);
    }

}
