package com.codeassessment.ledgerassement.app.excutor.service.command;

public class BookPostingEntryCommand {

  private final String transactionIdentifier;

  public BookPostingEntryCommand(final String transactionIdentifier) {
    super();
    this.transactionIdentifier = transactionIdentifier;
  }

  public String transactionIdentifier() {
    return this.transactionIdentifier;
  }

  @Override
  public String toString() {
    return "BookJournalEntryCommand{" +
            "transactionIdentifier='" + transactionIdentifier + '\'' +
            '}';
  }
}
