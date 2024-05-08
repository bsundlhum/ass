package com.codeassessment.ledgerassement.app.excutor.service.command;

public class DeleteLedgerCommand {

  private final String identifier;

  public DeleteLedgerCommand(final String identifier) {
    super();
    this.identifier = identifier;
  }

  public String identifier() {
    return this.identifier;
  }

  @Override
  public String toString() {
    return "DeleteLedgerCommand{" +
            "identifier='" + identifier + '\'' +
            '}';
  }
}
