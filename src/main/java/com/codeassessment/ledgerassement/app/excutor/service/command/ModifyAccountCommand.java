package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.Account;

public class ModifyAccountCommand {
  private final Account account;

  public ModifyAccountCommand(final Account account) {
    super();
    this.account = account;
  }

  public Account account() {
    return this.account;
  }

  @Override
  public String toString() {
    return "ModifyAccountCommand{" +
            "account=" + account.getIdentifier() +
            '}';
  }
}
