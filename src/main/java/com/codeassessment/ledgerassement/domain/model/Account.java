package com.codeassessment.ledgerassement.domain.model;

import java.util.Objects;
import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@SuppressWarnings({"unused", "WeakerAccess"})
public final class Account {

    private AccountType type;
    @Getter
    @Setter
    @Length(max = 34)
    private String identifier;
    @Getter
    @Setter
    @NotEmpty
    @Length(max = 256)
    private String name;
    @Getter
    @Setter
    private Set<String> holders;
    @Setter
    @Getter
    private Set<String> signatureAuthorities;
    @Setter
    @Getter
    @NotNull
    private Double balance;
    @Setter
    @Getter
    private String referenceAccount;
    @Getter
    @Setter
    private String ledger;
    private State state;
    @Getter
    @Setter
    private String createdOn;
    @Getter
    @Setter
    private String createdBy;
    @Getter
    @Setter
    private String lastModifiedOn;
    @Getter
    @Setter
    private String lastModifiedBy;

    public Account() {
        super();
    }

    public String getType() {
        return this.type.name();
    }

    public void setType(final String type) {
        this.type = AccountType.valueOf(type);
    }

    public String getState() {
        return this.state.name();
    }

    public void setState(final String state) {
        this.state = State.valueOf(state);
    }

    @SuppressWarnings("WeakerAccess")
    public enum State {
        OPEN,
        LOCKED,
        CLOSED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return type == account.type &&
                Objects.equals(identifier, account.identifier) &&
                Objects.equals(name, account.name) &&
                Objects.equals(holders, account.holders) &&
                Objects.equals(signatureAuthorities, account.signatureAuthorities) &&
                Objects.equals(balance, account.balance) &&
                Objects.equals(referenceAccount, account.referenceAccount) &&
                Objects.equals(ledger, account.ledger) &&
                state == account.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, identifier, name, holders, signatureAuthorities, balance, referenceAccount, ledger, state);
    }

    @Override
    public String toString() {
        return "Account{" +
                "type=" + type +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                ", holders=" + holders +
                ", signatureAuthorities=" + signatureAuthorities +
                ", balance=" + balance +
                ", referenceAccount='" + referenceAccount + '\'' +
                ", ledger='" + ledger + '\'' +
                ", state=" + state +
                ", createdOn='" + createdOn + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", lastModifiedOn='" + lastModifiedOn + '\'' +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                '}';
    }
}
