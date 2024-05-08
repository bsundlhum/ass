package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.Ledger;

public class ModifyLedgerCommand {

  private final Ledger ledger;

  public ModifyLedgerCommand(final Ledger ledger) {
    super();
    this.ledger = ledger;
  }

  public Ledger ledger() {
    return this.ledger;
  }

  @Override
  public String toString() {
    return "ModifyLedgerCommand{" +
            "ledger=" + ledger.getIdentifier() +
            '}';
  }
}
