package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.TransactionType;

public class ChangeTransactionTypeCommand {
  private final TransactionType transactionType;

  public ChangeTransactionTypeCommand(final TransactionType transactionType) {
    super();
    this.transactionType = transactionType;
  }

  public TransactionType transactionType() {
    return this.transactionType;
  }

  @Override
  public String toString() {
    return "ChangeTransactionTypeCommand{" +
            "transactionType=" + transactionType.getCode() +
            '}';
  }
}
