package com.codeassessment.ledgerassement.app.excutor.service.mapper;


import com.codeassessment.ledgerassement.app.excutor.service.repository.PostingsEntryEntity;
import com.codeassessment.ledgerassement.domain.model.Creditor;
import com.codeassessment.ledgerassement.domain.model.Debtor;
import com.codeassessment.ledgerassement.domain.model.PostingsEntry;

import java.util.stream.Collectors;

public class PostingsEntryMapper {

  private PostingsEntryMapper() {
    super();
  }

  public static PostingsEntry map(final PostingsEntryEntity journalEntryEntity) {
    final PostingsEntry journalEntry = new PostingsEntry();
    journalEntry.setTransactionIdentifier(journalEntryEntity.getTransactionIdentifier());
    journalEntry.setTransactionDate(journalEntryEntity.getTransactionDate().toString());
    journalEntry.setTransactionType(journalEntryEntity.getTransactionType());
    journalEntry.setClerk(journalEntryEntity.getClerk());
    journalEntry.setNote(journalEntryEntity.getNote());
    journalEntry.setDebtors(
        journalEntryEntity.getDebtors()
            .stream()
            .map(debtorType -> {
              final Debtor debtor = new Debtor();
              debtor.setAccountNumber(debtorType.getAccountNumber());
              debtor.setAmount(Double.toString(debtorType.getAmount()));
              return debtor;
            })
            .collect(Collectors.toSet())
    );
    journalEntry.setCreditors(
        journalEntryEntity.getCreditors()
            .stream()
            .map(creditorType -> {
              final Creditor creditor = new Creditor();
              creditor.setAccountNumber(creditorType.getAccountNumber());
              creditor.setAmount(Double.toString(creditorType.getAmount()));
              return creditor;
            })
            .collect(Collectors.toSet())
    );
    journalEntry.setMessage(journalEntryEntity.getMessage());
    journalEntry.setState(Enum.valueOf(PostingsEntry.State.class,journalEntryEntity.getState()));
    return journalEntry;
  }
}
