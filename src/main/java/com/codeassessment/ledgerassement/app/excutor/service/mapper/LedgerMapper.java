package com.codeassessment.ledgerassement.app.excutor.service.mapper;

import com.codeassessment.ledgerassement.app.excutor.service.repository.LedgerEntity;
import com.codeassessment.ledgerassement.domain.model.Ledger;

public class LedgerMapper {

  private LedgerMapper() {
    super();
  }

  public static Ledger map(final LedgerEntity ledgerEntity) {
    final Ledger ledger = new Ledger();
    ledger.setType(ledgerEntity.getType());
    ledger.setIdentifier(ledgerEntity.getIdentifier());
    ledger.setName(ledgerEntity.getName());
    ledger.setDescription(ledgerEntity.getDescription());
    if (ledgerEntity.getParentLedger() != null) {
      ledger.setParentLedgerIdentifier(ledgerEntity.getParentLedger().getIdentifier());
    }
    ledger.setCreatedBy(ledgerEntity.getCreatedBy());
    ledger.setCreatedOn(ledgerEntity.getCreatedOn().toString());
    if (ledgerEntity.getLastModifiedBy() != null) {
      ledger.setLastModifiedBy(ledgerEntity.getLastModifiedBy());
      ledger.setLastModifiedOn(ledgerEntity.getLastModifiedOn().toString());
    }
    ledger.setShowAccountsInChart(ledgerEntity.getShowAccountsInChart());
    return ledger;
  }
}
