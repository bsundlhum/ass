package com.codeassessment.ledgerassement.app.excutor.service.command;

public class LockAccountCommand {

  private final String identifier;
  private final String comment;

  public LockAccountCommand(final String identifier, final String comment) {
    super();
    this.identifier = identifier;
    this.comment = comment;
  }

  public String identifier() {
    return this.identifier;
  }

  public String comment() {
    return this.comment;
  }

  @Override
  public String toString() {
    return "LockAccountCommand{" +
            "identifier='" + identifier + '\'' +
            '}';
  }
}
