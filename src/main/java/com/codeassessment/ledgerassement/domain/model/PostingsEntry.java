package com.codeassessment.ledgerassement.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@SuppressWarnings({"unused", "WeakerAccess"})
public class PostingsEntry {
    @Size(max = 2200)
    private String transactionIdentifier;
    @NotNull
    private String transactionDate;

    private String transactionType;
    @NotEmpty
    private String clerk;
    private String note;
    @NotNull
    @Valid
    private Set<Debtor> debtors;
    @NotNull
    @Valid
    private Set<Creditor> creditors;
    private State state;
    @Size(max=2048)
    private String message;

    public PostingsEntry() {
        super();
    }
    @SuppressWarnings("WeakerAccess")
    public enum State {
        PENDING,
        PROCESSED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostingsEntry that = (PostingsEntry) o;
        return Objects.equals(transactionIdentifier, that.transactionIdentifier) &&
                Objects.equals(transactionDate, that.transactionDate) &&
                Objects.equals(transactionType, that.transactionType) &&
                Objects.equals(clerk, that.clerk) &&
                Objects.equals(note, that.note) &&
                Objects.equals(debtors, that.debtors) &&
                Objects.equals(creditors, that.creditors) &&
                state == that.state &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionIdentifier, transactionDate, transactionType, clerk, note, debtors, creditors, state, message);
    }

    @Override
    public String toString() {
        return "JournalEntry{" +
                "transactionIdentifier='" + transactionIdentifier + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", clerk='" + clerk + '\'' +
                ", note='" + note + '\'' +
                ", debtors=" + debtors +
                ", creditors=" + creditors +
                ", state=" + state +
                ", message='" + message + '\'' +
                '}';
    }
}
