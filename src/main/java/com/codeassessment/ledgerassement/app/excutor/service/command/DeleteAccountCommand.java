package com.codeassessment.ledgerassement.app.excutor.service.command;

public class DeleteAccountCommand {
  private final String identifier;

  public DeleteAccountCommand(final String identifier) {
    super();
    this.identifier = identifier;
  }

  public String identifier() {
    return this.identifier;
  }

  @Override
  public String toString() {
    return "DeleteAccountCommand{" +
            "identifier='" + identifier + '\'' +
            '}';
  }
}
