package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.Ledger;

public class AddSubLedgerCommand {

  private final String parentLedgerIdentifier;
  private final Ledger subLedger;

  public AddSubLedgerCommand(final String parentLedgerIdentifier, final Ledger subLedger) {
    super();
    this.parentLedgerIdentifier = parentLedgerIdentifier;
    this.subLedger = subLedger;
  }

  public String parentLedgerIdentifier() {
    return this.parentLedgerIdentifier;
  }

  public Ledger subLedger() {
    return this.subLedger;
  }

  @Override
  public String toString() {
    return "AddSubLedgerCommand{" +
            "parentLedgerIdentifier='" + parentLedgerIdentifier + '\'' +
            ", subLedger=" + subLedger.getIdentifier() +
            '}';
  }
}
