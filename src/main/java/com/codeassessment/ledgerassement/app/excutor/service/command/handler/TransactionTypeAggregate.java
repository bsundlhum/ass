package com.codeassessment.ledgerassement.app.excutor.service.command.handler;

import com.codeassessment.ledgerassement.app.excutor.service.command.ChangeTransactionTypeCommand;
import com.codeassessment.ledgerassement.app.excutor.service.command.CreateTransactionTypeCommand;
import com.codeassessment.ledgerassement.app.excutor.service.mapper.TransactionTypeMapper;
import com.codeassessment.ledgerassement.app.excutor.service.repository.*;
import com.codeassessment.ledgerassement.domain.EventConstants;
import com.codeassessment.ledgerassement.domain.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;



@SuppressWarnings("unused")
@Aggregate
public class TransactionTypeAggregate {
  private final TransactionTypeRepository transactionTypeRepository;

  @Autowired
  public TransactionTypeAggregate(final TransactionTypeRepository transactionTypeRepository) {
    super();
    this.transactionTypeRepository = transactionTypeRepository;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.DEBUG, logFinish = CommandLogLevel.DEBUG)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.POST_TX_TYPE)
  public String createTransactionType(final CreateTransactionTypeCommand createTransactionTypeCommand) {
    final TransactionType transactionType = createTransactionTypeCommand.transactionType();

    this.transactionTypeRepository.save(TransactionTypeMapper.map(transactionType));

    return transactionType.getCode();
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.DEBUG, logFinish = CommandLogLevel.DEBUG)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.PUT_TX_TYPE)
  public String changeTransactionType(final ChangeTransactionTypeCommand changeTransactionTypeCommand) {
    final TransactionType transactionType = changeTransactionTypeCommand.transactionType();

    final Optional<TransactionTypeEntity> optionalTransactionTypeEntity =
        this.transactionTypeRepository.findByIdentifier(transactionType.getCode());

    optionalTransactionTypeEntity.ifPresent(transactionTypeEntity -> {
      transactionTypeEntity.setName(transactionType.getName());
      transactionTypeEntity.setDescription(transactionType.getDescription());
      this.transactionTypeRepository.save(transactionTypeEntity);
    });

    return transactionType.getCode();
  }
}
