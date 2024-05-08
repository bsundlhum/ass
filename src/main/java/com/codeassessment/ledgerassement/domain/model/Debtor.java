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
public final class Debtor {
    @Size(max = 34)
    private String accountNumber;
    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private String amount;

    public Debtor() {
        super();
    }

    public Debtor(String accountNumber, String amount) {
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debtor debtor = (Debtor) o;
        return Objects.equals(accountNumber, debtor.accountNumber) &&
                Objects.equals(amount, debtor.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, amount);
    }

    @Override
    public String toString() {
        return "Debtor{" +
                "accountNumber='" + accountNumber + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
