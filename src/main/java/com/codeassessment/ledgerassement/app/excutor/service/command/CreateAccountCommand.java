package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.Account;

public class CreateAccountCommand {
  private final Account account;

  public CreateAccountCommand(final Account account) {
    super();
    this.account = account;
  }

  public Account account() {
    return this.account;
  }

  @Override
  public String toString() {
    return "CreateAccountCommand{" +
            "account=" + account.getIdentifier() +
            '}';
  }
}
