package com.codeassessment.ledgerassement.app.excutor.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerEntity, Long>, JpaSpecificationExecutor<LedgerEntity> {

  List<LedgerEntity> findByParentLedgerIsNull();

  List<LedgerEntity> findByParentLedgerOrderByIdentifier(final LedgerEntity parentLedger);

  LedgerEntity findByIdentifier(final String identifier);
}
