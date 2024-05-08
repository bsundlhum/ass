package com.codeassessment.ledgerassement.app.excutor.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandRepository extends JpaRepository<CommandEntity, Long> {

  List<CommandEntity> findByAccount(final AccountEntity accountEntity);
}
