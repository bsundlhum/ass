package com.codeassessment.ledgerassement.app.excutor.service.services;

import com.codeassessment.ledgerassement.app.excutor.service.mapper.LedgerMapper;
import com.codeassessment.ledgerassement.app.excutor.service.repository.AccountEntity;
import com.codeassessment.ledgerassement.app.excutor.service.repository.AccountRepository;
import com.codeassessment.ledgerassement.app.excutor.service.repository.LedgerEntity;
import com.codeassessment.ledgerassement.app.excutor.service.repository.LedgerRepository;
import com.codeassessment.ledgerassement.domain.model.AccountType;
import com.codeassessment.ledgerassement.domain.model.TrialBalance;
import com.codeassessment.ledgerassement.domain.model.TrialBalanceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TrialBalanceService {

  private final LedgerRepository ledgerRepository;
  private final AccountRepository accountRepository;

  @Autowired
  public TrialBalanceService(final LedgerRepository ledgerRepository,
                             final AccountRepository accountRepository) {
    super();
    this.ledgerRepository = ledgerRepository;
    this.accountRepository = accountRepository;
  }

  public TrialBalance getTrialBalance(final boolean includeEmptyEntries) {
    final TrialBalance trialBalance = new TrialBalance();
    final List<LedgerEntity> ledgers = this.ledgerRepository.findAll();
    if (ledgers != null) ledgers.forEach(ledgerEntity -> {
      final List<AccountEntity> accountEntities = this.accountRepository.findByLedger(ledgerEntity);
      if (accountEntities != null) {
        final TrialBalanceEntry trialBalanceEntry = new TrialBalanceEntry();
        trialBalanceEntry.setLedger(LedgerMapper.map(ledgerEntity));
        switch (AccountType.valueOf(ledgerEntity.getType())) {
          case ASSET:
          case EXPENSE:
            trialBalanceEntry.setType(TrialBalanceEntry.Type.valueOf(TrialBalanceEntry.Type.DEBIT.name()));
            break;
          case LIABILITY:
          case EQUITY:
          case REVENUE:
            trialBalanceEntry.setType(TrialBalanceEntry.Type.valueOf(TrialBalanceEntry.Type.CREDIT.name()));
            break;
        }
        trialBalanceEntry.setAmount(0.00D);
        accountEntities.forEach(accountEntity ->
                trialBalanceEntry.setAmount(trialBalanceEntry.getAmount() + accountEntity.getBalance()));
        if (!includeEmptyEntries && trialBalanceEntry.getAmount() == 0.00D) {
          //noinspection UnnecessaryReturnStatement
          return;
        } else {
          trialBalance.getTrialBalanceEntries().add(trialBalanceEntry);
        }
      }
    });

    trialBalance.setDebitTotal(
        trialBalance.getTrialBalanceEntries()
            .stream()
            .filter(trialBalanceEntry -> trialBalanceEntry.getType().toString().equals(TrialBalanceEntry.Type.DEBIT.name()))
            .mapToDouble(TrialBalanceEntry::getAmount)
            .sum()
    );

    trialBalance.setCreditTotal(
        trialBalance.getTrialBalanceEntries()
            .stream()
            .filter(trialBalanceEntry -> trialBalanceEntry.getType().toString().equals(TrialBalanceEntry.Type.CREDIT.name()))
            .mapToDouble(TrialBalanceEntry::getAmount)
            .sum()
    );

    // Sort by ledger identifier ASC
    trialBalance.getTrialBalanceEntries().sort(Comparator.comparing(o -> o.getLedger().getIdentifier()));

    return trialBalance;
  }
}
