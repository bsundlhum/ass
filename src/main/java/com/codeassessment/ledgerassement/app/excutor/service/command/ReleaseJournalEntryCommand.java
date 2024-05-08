package com.codeassessment.ledgerassement.app.excutor.service.command;

public class ReleaseJournalEntryCommand {

  private final String transactionIdentifier;

  public ReleaseJournalEntryCommand(final String transactionIdentifier) {
    super();
    this.transactionIdentifier = transactionIdentifier;
  }

  public String transactionIdentifier() {
    return this.transactionIdentifier;
  }

  @Override
  public String toString() {
    return "ReleaseJournalEntryCommand{" +
            "transactionIdentifier='" + transactionIdentifier + '\'' +
            '}';
  }
}
