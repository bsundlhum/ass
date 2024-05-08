package com.codeassessment.ledgerassement.app.excutor.service.command.handler;

import com.codeassessment.ledgerassement.app.excutor.service.command.BookPostingEntryCommand;
import com.codeassessment.ledgerassement.app.excutor.service.command.CreatePostingsEntryCommand;
import com.codeassessment.ledgerassement.app.excutor.service.command.ReleaseJournalEntryCommand;
import com.codeassessment.ledgerassement.app.excutor.service.repository.*;
import com.codeassessment.ledgerassement.app.excutor.service.services.CommandGateway;
import com.codeassessment.ledgerassement.app.excutor.util.UserContextHolder;
import com.codeassessment.ledgerassement.domain.EventConstants;
import com.codeassessment.ledgerassement.domain.model.Creditor;
import com.codeassessment.ledgerassement.domain.model.Debtor;
import com.codeassessment.ledgerassement.domain.model.PostingsEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Aggregate
public class PostingsEntryCommandHandler {

  private final CommandGateway commandGateway;
  private final PostingsEntryRepository journalEntryRepository;

  @Autowired
  public PostingsEntryCommandHandler(final CommandGateway commandGateway,
                                    final PostingsEntryRepository journalEntryRepository) {
    this.commandGateway = commandGateway;
    this.journalEntryRepository = journalEntryRepository;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.NONE, logFinish = CommandLogLevel.NONE)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.POST_JOURNAL_ENTRY)
  public String createJournalEntry(final CreatePostingsEntryCommand createJournalEntryCommand) {
    final PostingsEntry journalEntry = createJournalEntryCommand.postingsEntry();
    final Set<Debtor> debtors = journalEntry.getDebtors();
    final Set<DebtorType> debtorTypes = debtors
        .stream()
        .map(debtor -> {
          final DebtorType debtorType = new DebtorType();
          debtorType.setAccountNumber(debtor.getAccountNumber());
          debtorType.setAmount(Double.valueOf(debtor.getAmount()));
          return debtorType;
        })
        .collect(Collectors.toSet());
    final Set<Creditor> creditors = journalEntry.getCreditors();
    final Set<CreditorType> creditorTypes = creditors
        .stream()
        .map(creditor -> {
          final CreditorType creditorType = new CreditorType();
          creditorType.setAccountNumber(creditor.getAccountNumber());
          creditorType.setAmount(Double.valueOf(creditor.getAmount()));
          return creditorType;
        })
        .collect(Collectors.toSet());
    final PostingsEntryEntity journalEntryEntity = new PostingsEntryEntity();
    journalEntryEntity.setTransactionIdentifier(journalEntry.getTransactionIdentifier());
    final LocalDateTime transactionDate = LocalDateTime.parse(journalEntry.getTransactionDate());
    journalEntryEntity.setDateBucket(String.valueOf(transactionDate));
    journalEntryEntity.setTransactionDate(transactionDate);
    journalEntryEntity.setTransactionType(journalEntry.getTransactionType());
    journalEntryEntity.setClerk(journalEntry.getClerk() != null ? journalEntry.getClerk() : UserContextHolder.checkedGetUser());
    journalEntryEntity.setNote(journalEntry.getNote());
    journalEntryEntity.setDebtors(debtorTypes);
    journalEntryEntity.setCreditors(creditorTypes);
    journalEntryEntity.setMessage(journalEntry.getMessage());
    journalEntryEntity.setState(PostingsEntry.State.PENDING.name());
    journalEntryEntity.setCreatedBy(UserContextHolder.checkedGetUser());
    journalEntryEntity.setCreatedOn(LocalDateTime.now(Clock.systemUTC()));
    journalEntryRepository.saveJournalEntry(journalEntryEntity);
    this.commandGateway.process(new BookPostingEntryCommand(journalEntry.getTransactionIdentifier()));
    return journalEntry.getTransactionIdentifier();
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.NONE, logFinish = CommandLogLevel.NONE)
  public void releaseJournalEntry(final ReleaseJournalEntryCommand releaseJournalEntryCommand) {
    final String transactionIdentifier = releaseJournalEntryCommand.transactionIdentifier();
    final Optional<PostingsEntryEntity> optionalJournalEntry = this.journalEntryRepository.findJournalEntry(transactionIdentifier);
    if (optionalJournalEntry.isPresent()) {
      final PostingsEntryEntity journalEntryEntity = optionalJournalEntry.get();
      journalEntryEntity.setState(PostingsEntry.State.PROCESSED.name());
      this.journalEntryRepository.saveJournalEntry(journalEntryEntity);
    }
  }
}
