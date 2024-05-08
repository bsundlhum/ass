package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.Ledger;

public class CreateLedgerCommand {

  private final Ledger ledger;

  public CreateLedgerCommand(final Ledger ledger) {
    super();
    this.ledger = ledger;
  }

  public Ledger ledger() {
    return this.ledger;
  }

  @Override
  public String toString() {
    return "CreateLedgerCommand{" +
            "ledger=" + ledger.getIdentifier() +
            '}';
  }
}
