package com.codeassessment.ledgerassement.app.excutor.service.mapper;

import com.codeassessment.ledgerassement.app.excutor.service.repository.AccountEntryEntity;
import com.codeassessment.ledgerassement.domain.model.AccountEntry;

public class AccountEntryMapper {

  private AccountEntryMapper() {
    super();
  }

  public static AccountEntry map(final AccountEntryEntity accountEntity) {
    final AccountEntry entry = new AccountEntry();

    entry.setType(Enum.valueOf(AccountEntry.Type.class,accountEntity.getType()));
    entry.setBalance(accountEntity.getBalance());
    entry.setAmount(accountEntity.getAmount());
    entry.setMessage(accountEntity.getMessage());
    entry.setTransactionDate(accountEntity.getTransactionDate().toString());

    return entry;
  }
}
