package com.codeassessment.ledgerassement.domain.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@SuppressWarnings({"unused", "WeakerAccess"})
public final class Creditor {
    @Size(max = 34)
    private String accountNumber;
    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private String amount;

    public Creditor() {
        super();
    }

    public Creditor(String accountNumber, String amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Creditor creditor = (Creditor) o;
        return Objects.equals(accountNumber, creditor.accountNumber) &&
                Objects.equals(amount, creditor.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, amount);
    }

    @Override
    public String toString() {
        return "Creditor{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
