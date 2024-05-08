package com.codeassessment.ledgerassement.app.excutor.service.repository;

import com.datastax.driver.mapping.annotations.Field;
import com.datastax.driver.mapping.annotations.UDT;

@SuppressWarnings({"unused"})
@UDT(name = "assessledge_debtor")
public class DebtorType {

  @Field(name = "account_number")
  private String accountNumber;
  @Field(name = "amount")
  private Double amount;

  public DebtorType() {
    super();
  }

  public String getAccountNumber() {
    return this.accountNumber;
  }

  public void setAccountNumber(final String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public Double getAmount() {
    return this.amount;
  }

  public void setAmount(final Double amount) {
    this.amount = amount;
  }
}
