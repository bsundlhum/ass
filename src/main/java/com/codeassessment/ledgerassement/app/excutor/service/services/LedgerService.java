package com.codeassessment.ledgerassement.app.excutor.service.services;


import com.codeassessment.ledgerassement.app.excutor.service.mapper.AccountMapper;
import com.codeassessment.ledgerassement.app.excutor.service.mapper.LedgerMapper;
import com.codeassessment.ledgerassement.app.excutor.service.repository.AccountEntity;
import com.codeassessment.ledgerassement.app.excutor.service.repository.AccountRepository;
import com.codeassessment.ledgerassement.app.excutor.service.repository.LedgerEntity;
import com.codeassessment.ledgerassement.app.excutor.service.repository.LedgerRepository;
import com.codeassessment.ledgerassement.app.excutor.service.repository.specification.LedgerSpecification;
import com.codeassessment.ledgerassement.domain.model.Account;
import com.codeassessment.ledgerassement.domain.model.AccountAPI;
import com.codeassessment.ledgerassement.domain.model.Ledger;
import com.codeassessment.ledgerassement.domain.model.LedgerAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LedgerService {

  private final LedgerRepository ledgerRepository;
  private final AccountRepository accountRepository;

  @Autowired
  public LedgerService(final LedgerRepository ledgerRepository,
                       final AccountRepository accountRepository) {
    super();
    this.ledgerRepository = ledgerRepository;
    this.accountRepository = accountRepository;
  }

  public LedgerAPI fetchLedgers(final boolean includeSubLedgers,
                                final String term,
                                final String type,
                                final Pageable pageable) {
    final LedgerAPI ledgerPage = new LedgerAPI();

    final Page<LedgerEntity> ledgerEntities = this.ledgerRepository.findAll(
        LedgerSpecification.createSpecification(includeSubLedgers, term, type), pageable
    );

    ledgerPage.setTotalPages(ledgerEntities.getTotalPages());
    ledgerPage.setTotalElements(ledgerEntities.getTotalElements());

    ledgerPage.setLedgers(this.mapToLedger(ledgerEntities.getContent()));

    return ledgerPage;
  }

  private List<Ledger> mapToLedger(List<LedgerEntity> ledgerEntities) {
    final List<Ledger> result = new ArrayList<>(ledgerEntities.size());

    if(!ledgerEntities.isEmpty()) {
      ledgerEntities.forEach(ledgerEntity -> {
        final Ledger ledger = LedgerMapper.map(ledgerEntity);
        this.addSubLedgers(ledger, this.ledgerRepository.findByParentLedgerOrderByIdentifier(ledgerEntity));
        result.add(ledger);
      });
    }

    return result;
  }

  public Optional<Ledger> findLedger(final String identifier) {
    final LedgerEntity ledgerEntity = this.ledgerRepository.findByIdentifier(identifier);
    if (ledgerEntity != null) {
      final Ledger ledger = LedgerMapper.map(ledgerEntity);
      this.addSubLedgers(ledger, this.ledgerRepository.findByParentLedgerOrderByIdentifier(ledgerEntity));
      return Optional.of(ledger);
    } else {
      return Optional.empty();
    }
  }

  public AccountAPI fetchAccounts(final String ledgerIdentifier, final Pageable pageable) {
    final LedgerEntity ledgerEntity = this.ledgerRepository.findByIdentifier(ledgerIdentifier);
    final Page<AccountEntity> accountEntities = this.accountRepository.findByLedger(ledgerEntity, pageable);

    final AccountAPI accountPage = new AccountAPI();
    accountPage.setTotalPages(accountEntities.getTotalPages());
    accountPage.setTotalElements(accountEntities.getTotalElements());

    if(accountEntities.getSize() > 0){
      final List<Account> accounts = new ArrayList<>(accountEntities.getSize());
      accountEntities.forEach(accountEntity -> accounts.add(AccountMapper.map(accountEntity)));
      accountPage.setAccounts(accounts);
    }

    return accountPage;
  }

  public boolean hasAccounts(final String ledgerIdentifier) {
    final LedgerEntity ledgerEntity = this.ledgerRepository.findByIdentifier(ledgerIdentifier);
    final List<AccountEntity> ledgerAccounts = this.accountRepository.findByLedger(ledgerEntity);
    return ledgerAccounts.size() > 0;
  }

  private void addSubLedgers(final Ledger parentLedger,
                             final List<LedgerEntity> subLedgerEntities) {
    if (subLedgerEntities != null) {
      final List<Ledger> subLedgers = new ArrayList<>(subLedgerEntities.size());
      subLedgerEntities.forEach(subLedgerEntity -> subLedgers.add(LedgerMapper.map(subLedgerEntity)));
      parentLedger.setSubLedgers(subLedgers);
    }
  }
}
