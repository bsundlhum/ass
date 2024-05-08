package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.TransactionType;

public class CreateTransactionTypeCommand {
  private final TransactionType transactionType;

  public CreateTransactionTypeCommand(final TransactionType transactionType) {
    super();
    this.transactionType = transactionType;
  }

  public TransactionType transactionType() {
    return this.transactionType;
  }

  @Override
  public String toString() {
    return "CreateTransactionTypeCommand{" +
            "transactionType=" + transactionType.getCode() +
            '}';
  }
}
