package com.codeassessment.ledgerassement.domain.model;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Objects;

@Setter
@Getter
@SuppressWarnings({"unused", "WeakerAccess"})
public class AccountEntry {
    private Type type;
    private String transactionDate;
    private String message;
    private Double amount;
    private Double balance;

    public enum Type {
        DEBIT,
        CREDIT
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountEntry that = (AccountEntry) o;
        return type == that.type &&
                Objects.equals(transactionDate, that.transactionDate) &&
                Objects.equals(message, that.message) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, transactionDate, message, amount, balance);
    }
}
