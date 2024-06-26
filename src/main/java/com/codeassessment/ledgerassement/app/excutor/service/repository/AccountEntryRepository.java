package com.codeassessment.ledgerassement.app.excutor.service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface AccountEntryRepository extends JpaRepository<AccountEntryEntity, Long> {

  Page<AccountEntryEntity> findByAccountAndTransactionDateBetween(final AccountEntity accountEntity,
                                                                  final LocalDateTime dateFrom,
                                                                  final LocalDateTime dateTo,
                                                                  final Pageable pageable);

  Page<AccountEntryEntity> findByAccountAndTransactionDateBetweenAndMessageEquals(final AccountEntity accountEntity,
                                                                                  final LocalDateTime dateFrom,
                                                                                  final LocalDateTime dateTo,
                                                                                  final String message,
                                                                                  final Pageable pageable);


  @Query("SELECT CASE WHEN count(a) > 0 THEN true ELSE false END FROM AccountEntryEntity a where a.account = :accountEntity")
  Boolean existsByAccount(@Param("accountEntity") final AccountEntity accountEntity);
}
