package com.codeassessment.ledgerassement.app.excutor.service.command;

import com.codeassessment.ledgerassement.domain.model.PostingsEntry;

public class CreatePostingsEntryCommand {

  private final PostingsEntry postingsEntry;

  public CreatePostingsEntryCommand(final PostingsEntry journalEntry) {
    this.postingsEntry = journalEntry;
  }

  public PostingsEntry postingsEntry() {
    return this.postingsEntry;
  }

  @Override
  public String toString() {
    return "CreateJournalEntryCommand{" +
            "postingsEntry=" + postingsEntry.getTransactionIdentifier() +
            '}';
  }
}
