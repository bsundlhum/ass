package com.codeassessment.ledgerassement.app.excutor.service.services;

import com.codeassessment.ledgerassement.app.excutor.service.mapper.TransactionTypeMapper;
import com.codeassessment.ledgerassement.app.excutor.service.repository.TransactionTypeEntity;
import com.codeassessment.ledgerassement.app.excutor.service.repository.TransactionTypeRepository;
import com.codeassessment.ledgerassement.domain.model.TransactionType;
import com.codeassessment.ledgerassement.domain.model.TransactionTypeAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class TransactionTypeService {

  private final TransactionTypeRepository transactionTypeRepository;

  @Autowired
  public TransactionTypeService(final TransactionTypeRepository transactionTypeRepository) {
    super();
    this.transactionTypeRepository = transactionTypeRepository;
  }

  public TransactionTypeAPI fetchTransactionTypes(final String term, final Pageable pageable) {
    final Page<TransactionTypeEntity> transactionTypeEntityPage;
    if (term != null) {
      transactionTypeEntityPage =
          this.transactionTypeRepository.findByIdentifierContainingOrNameContaining(term, term, pageable);
    } else {
      transactionTypeEntityPage = this.transactionTypeRepository.findAll(pageable);
    }

    final TransactionTypeAPI transactionTypePage = new TransactionTypeAPI();
    transactionTypePage.setTotalElements(transactionTypeEntityPage.getTotalElements());
    transactionTypePage.setTotalPages(transactionTypeEntityPage.getTotalPages());

    transactionTypePage.setTransactionTypes(new ArrayList<>(transactionTypeEntityPage.getSize()));
    transactionTypeEntityPage.forEach(transactionTypeEntity ->
        transactionTypePage.add(TransactionTypeMapper.map(transactionTypeEntity)));

    return transactionTypePage;
  }

  public Optional<TransactionType> findByIdentifier(final String identifier) {
    return this.transactionTypeRepository.findByIdentifier(identifier).map(TransactionTypeMapper::map);
  }
}
