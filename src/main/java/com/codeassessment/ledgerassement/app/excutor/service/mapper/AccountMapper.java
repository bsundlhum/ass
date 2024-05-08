package com.codeassessment.ledgerassement.app.excutor.service.mapper;

import com.codeassessment.ledgerassement.app.excutor.service.repository.AccountEntity;
import com.codeassessment.ledgerassement.domain.model.Account;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;

public class AccountMapper {

  private AccountMapper() {
    super();
  }

  public static Account map(final AccountEntity accountEntity) {
    final Account account = new Account();
    account.setIdentifier(accountEntity.getIdentifier());
    account.setName(accountEntity.getName());
    account.setType(accountEntity.getType());
    account.setLedger(accountEntity.getLedger().getIdentifier());

    if (accountEntity.getHolders() != null) {
      account.setHolders(
              new HashSet<>(Arrays.asList(StringUtils.split(accountEntity.getHolders(), ",")))
      );
    }

    if (accountEntity.getSignatureAuthorities() != null) {
      account.setSignatureAuthorities(
          new HashSet<>(Arrays.asList(StringUtils.split(accountEntity.getSignatureAuthorities(), ",")))
      );
    }
    if (accountEntity.getReferenceAccount() != null) {
      account.setReferenceAccount(accountEntity.getReferenceAccount().getIdentifier());
    }
    account.setBalance(accountEntity.getBalance());
    account.setCreatedBy(accountEntity.getCreatedBy());
    account.setCreatedOn(accountEntity.getCreatedOn().toString());
    if (accountEntity.getLastModifiedBy() != null) {
      account.setLastModifiedBy(accountEntity.getLastModifiedBy());
      account.setLastModifiedOn(accountEntity.getLastModifiedOn().toString());
    }
    account.setState(accountEntity.getState());
    return account;
  }
}
