package com.codeassessment.ledgerassement.app.excutor.service.command.handler;


import com.codeassessment.ledgerassement.app.excutor.service.command.*;
import com.codeassessment.ledgerassement.app.excutor.service.repository.*;
import com.codeassessment.ledgerassement.app.excutor.service.services.CommandGateway;
import com.codeassessment.ledgerassement.app.excutor.util.ServiceException;
import com.codeassessment.ledgerassement.app.excutor.util.UserContextHolder;
import com.codeassessment.ledgerassement.domain.EventConstants;
import com.codeassessment.ledgerassement.domain.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Aggregate
public class AccountCommandHandler {

  private final CommandGateway commandGateway;
  private final AccountRepository accountRepository;
  private final AccountEntryRepository accountEntryRepository;
  private final LedgerRepository ledgerRepository;
  private final PostingsEntryRepository journalEntryRepository;
  private final CommandRepository commandRepository;

  @Autowired
  public AccountCommandHandler(//@Qualifier(ThothServiceConstants.LOGGER_NAME) final Logger logger,
                               final CommandGateway commandGateway,
                               final AccountRepository accountRepository,
                               final AccountEntryRepository accountEntryRepository,
                               final LedgerRepository ledgerRepository,
                               final PostingsEntryRepository journalEntryRepository,
                               final CommandRepository commandRepository) {
    super();
    this.commandGateway = commandGateway;
    this.accountRepository = accountRepository;
    this.accountEntryRepository = accountEntryRepository;
    this.ledgerRepository = ledgerRepository;
    this.journalEntryRepository = journalEntryRepository;
    this.commandRepository = commandRepository;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.POST_ACCOUNT)
  public String createAccount(final CreateAccountCommand createAccountCommand) {
    final Account account = createAccountCommand.account();
    final AccountEntity accountEntity = new AccountEntity();
    accountEntity.setIdentifier(account.getIdentifier());
    accountEntity.setName(account.getName());
    accountEntity.setType(account.getType());

    final LedgerEntity ledger = this.ledgerRepository.findByIdentifier(account.getLedger());
    accountEntity.setLedger(ledger);

    AccountEntity referenceAccount = null;
    if (account.getReferenceAccount() != null) {
      referenceAccount = this.accountRepository.findByIdentifier(account.getReferenceAccount());
      if (referenceAccount.getState().equals(Account.State.OPEN.name())) {
        accountEntity.setReferenceAccount(referenceAccount);
      } else {
        throw ServiceException.badRequest("Reference account {0} is not valid.", referenceAccount.getIdentifier());
      }
    }

    if (account.getHolders() != null) {
      accountEntity.setHolders(
              account.getHolders()
                      .stream()
                      .collect(Collectors.joining(","))
      );
    }

    if (account.getSignatureAuthorities() != null) {
      accountEntity.setSignatureAuthorities(
          account.getSignatureAuthorities()
              .stream()
              .collect(Collectors.joining(","))
      );
    }

    accountEntity.setBalance(account.getBalance());
    accountEntity.setState(Account.State.OPEN.name());
    accountEntity.setCreatedBy(UserContextHolder.checkedGetUser());
    accountEntity.setCreatedOn(LocalDateTime.now(Clock.systemUTC()));

    this.accountRepository.save(accountEntity);

    if (referenceAccount != null) {
      referenceAccount.setLastModifiedBy(UserContextHolder.checkedGetUser());
      referenceAccount.setLastModifiedOn(LocalDateTime.now(Clock.systemUTC()));
      this.accountRepository.save(referenceAccount);
    }

    this.ledgerRepository.save(ledger);

    return account.getIdentifier();
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.PUT_ACCOUNT)
  public String modifyAccount(final ModifyAccountCommand modifyAccountCommand) {
    final Account account = modifyAccountCommand.account();
    final AccountEntity accountEntity = this.accountRepository.findByIdentifier(account.getIdentifier());

    if (account.getName() != null) {
      accountEntity.setName(account.getName());
    }

    LedgerEntity ledger = null;
    if (!account.getLedger().equals(accountEntity.getLedger().getIdentifier())) {
      ledger = this.ledgerRepository.findByIdentifier(account.getLedger());
      accountEntity.setLedger(ledger);
    }

    AccountEntity referenceAccount = null;
    if (account.getReferenceAccount() != null) {
      if (!account.getReferenceAccount().equals(accountEntity.getReferenceAccount().getIdentifier())) {
        referenceAccount = this.accountRepository.findByIdentifier(account.getReferenceAccount());
        accountEntity.setReferenceAccount(referenceAccount);
      }
    } else {
      accountEntity.setReferenceAccount(null);
    }

    if (account.getHolders() != null) {
      accountEntity.setHolders(
              account.getHolders()
                      .stream()
                      .collect(Collectors.joining(","))
      );
    } else {
      accountEntity.setHolders(null);
    }

    if (account.getSignatureAuthorities() != null) {
      accountEntity.setSignatureAuthorities(
          account.getSignatureAuthorities()
              .stream()
              .collect(Collectors.joining(","))
      );
    } else {
      accountEntity.setSignatureAuthorities(null);
    }

    accountEntity.setLastModifiedBy(UserContextHolder.checkedGetUser());
    accountEntity.setLastModifiedOn(LocalDateTime.now(Clock.systemUTC()));

    this.accountRepository.save(accountEntity);

    if (referenceAccount != null) {
      referenceAccount.setLastModifiedBy(UserContextHolder.checkedGetUser());
      referenceAccount.setLastModifiedOn(LocalDateTime.now(Clock.systemUTC()));
      this.accountRepository.save(referenceAccount);
    }

    if (ledger != null) {
      this.ledgerRepository.save(ledger);
    }

    return account.getIdentifier();
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.CLOSE_ACCOUNT)
  public String closeAccount(final CloseAccountCommand closeAccountCommand) {
    final String modifyingUser = SecurityContextHolder.getContext().getAuthentication().getName();
    final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    final String identifier = closeAccountCommand.identifier();

    final AccountEntity accountEntity = this.accountRepository.findByIdentifier(identifier);
    accountEntity.setState(Account.State.CLOSED.name());
    accountEntity.setLastModifiedBy(modifyingUser);
    accountEntity.setLastModifiedOn(now);
    this.accountRepository.save(accountEntity);

    final CommandEntity commandEntity = new CommandEntity();
    commandEntity.setType(AccountCommand.Action.CLOSE.name());
    commandEntity.setAccount(accountEntity);
    commandEntity.setComment(closeAccountCommand.comment());
    commandEntity.setCreatedBy(modifyingUser);
    commandEntity.setCreatedOn(now);
    this.commandRepository.save(commandEntity);

    return identifier;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.LOCK_ACCOUNT)
  public String lockAccount(final LockAccountCommand lockAccountCommand) {
    final String modifyingUser = SecurityContextHolder.getContext().getAuthentication().getName();
    final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    final String identifier = lockAccountCommand.identifier();

    final AccountEntity accountEntity = this.accountRepository.findByIdentifier(identifier);
    accountEntity.setState(Account.State.LOCKED.name());
    accountEntity.setLastModifiedBy(modifyingUser);
    accountEntity.setLastModifiedOn(now);
    this.accountRepository.save(accountEntity);

    final CommandEntity commandEntity = new CommandEntity();
    commandEntity.setType(AccountCommand.Action.LOCK.name());
    commandEntity.setAccount(accountEntity);
    commandEntity.setComment(lockAccountCommand.comment());
    commandEntity.setCreatedBy(modifyingUser);
    commandEntity.setCreatedOn(now);
    this.commandRepository.save(commandEntity);

    return identifier;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.UNLOCK_ACCOUNT)
  public String unlockAccount(final UnlockAccountCommand unlockAccountCommand) {
    final String modifyingUser = SecurityContextHolder.getContext().getAuthentication().getName();
    final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    final String identifier = unlockAccountCommand.identifier();

    final AccountEntity accountEntity = this.accountRepository.findByIdentifier(identifier);
    accountEntity.setState(Account.State.OPEN.name());
    accountEntity.setLastModifiedBy(modifyingUser);
    accountEntity.setLastModifiedOn(now);
    this.accountRepository.save(accountEntity);

    final CommandEntity commandEntity = new CommandEntity();
    commandEntity.setType(AccountCommand.Action.UNLOCK.name());
    commandEntity.setAccount(accountEntity);
    commandEntity.setComment(unlockAccountCommand.comment());
    commandEntity.setCreatedBy(modifyingUser);
    commandEntity.setCreatedOn(now);
    this.commandRepository.save(commandEntity);

    return identifier;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.REOPEN_ACCOUNT)
  public String reopenAccount(final ReopenAccountCommand reopenAccountCommand) {
    final String modifyingUser = SecurityContextHolder.getContext().getAuthentication().getName();
    final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
    final String identifier = reopenAccountCommand.identifier();

    final AccountEntity accountEntity = this.accountRepository.findByIdentifier(identifier);
    accountEntity.setState(Account.State.OPEN.name());
    accountEntity.setLastModifiedBy(modifyingUser);
    accountEntity.setLastModifiedOn(now);
    this.accountRepository.save(accountEntity);

    final CommandEntity commandEntity = new CommandEntity();
    commandEntity.setType(AccountCommand.Action.REOPEN.name());
    commandEntity.setAccount(accountEntity);
    commandEntity.setComment(reopenAccountCommand.comment());
    commandEntity.setCreatedBy(modifyingUser);
    commandEntity.setCreatedOn(now);
    this.commandRepository.save(commandEntity);

    return identifier;
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.NONE, logFinish = CommandLogLevel.NONE)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.RELEASE_JOURNAL_ENTRY)
  public String bookJournalEntry(final BookPostingEntryCommand bookJournalEntryCommand) {
    final String transactionIdentifier = bookJournalEntryCommand.transactionIdentifier();

    final Optional<PostingsEntryEntity> optionalJournalEntry = this.journalEntryRepository.findJournalEntry(transactionIdentifier);
    if (optionalJournalEntry.isPresent()) {
      final PostingsEntryEntity journalEntryEntity = optionalJournalEntry.get();
      if (!journalEntryEntity.getState().equals(PostingsEntry.State.PENDING.name())) {
        return null;
      }
      // process all debtors
      journalEntryEntity.getDebtors()
          .forEach(debtor -> {
            final String accountNumber = debtor.getAccountNumber();
            final AccountEntity accountEntity = this.accountRepository.findByIdentifier(accountNumber);
            final AccountType accountType = AccountType.valueOf(accountEntity.getType());
            switch (accountType) {
              case ASSET:
              case EXPENSE:
                accountEntity.setBalance(accountEntity.getBalance() + debtor.getAmount());
                break;
              case LIABILITY:
              case EQUITY:
              case REVENUE:
                accountEntity.setBalance(accountEntity.getBalance() - debtor.getAmount());
                break;
            }
            final AccountEntity savedAccountEntity = this.accountRepository.save(accountEntity);
            final AccountEntryEntity accountEntryEntity = new AccountEntryEntity();
            accountEntryEntity.setType(AccountEntry.Type.DEBIT.name());
            accountEntryEntity.setAccount(savedAccountEntity);
            accountEntryEntity.setBalance(savedAccountEntity.getBalance());
            accountEntryEntity.setAmount(debtor.getAmount());
            accountEntryEntity.setMessage(journalEntryEntity.getMessage());
            accountEntryEntity.setTransactionDate(journalEntryEntity.getTransactionDate());
            this.accountEntryRepository.save(accountEntryEntity);
          });
      // process all creditors
      journalEntryEntity.getCreditors()
          .forEach(creditor -> {
            final String accountNumber = creditor.getAccountNumber();
            final AccountEntity accountEntity = this.accountRepository.findByIdentifier(accountNumber);
            final AccountType accountType = AccountType.valueOf(accountEntity.getType());
            switch (accountType) {
              case ASSET:
              case EXPENSE:
                accountEntity.setBalance(accountEntity.getBalance() - creditor.getAmount());
                break;
              case LIABILITY:
              case EQUITY:
              case REVENUE:
                accountEntity.setBalance(accountEntity.getBalance() + creditor.getAmount());
                break;
            }
            final AccountEntity savedAccountEntity = this.accountRepository.save(accountEntity);
            final AccountEntryEntity accountEntryEntity = new AccountEntryEntity();
            accountEntryEntity.setType(AccountEntry.Type.CREDIT.name());
            accountEntryEntity.setAccount(savedAccountEntity);
            accountEntryEntity.setBalance(savedAccountEntity.getBalance());
            accountEntryEntity.setAmount(creditor.getAmount());
            accountEntryEntity.setMessage(journalEntryEntity.getMessage());
            accountEntryEntity.setTransactionDate(journalEntryEntity.getTransactionDate());
            this.accountEntryRepository.save(accountEntryEntity);
          });
      this.commandGateway.process(new ReleaseJournalEntryCommand(transactionIdentifier));
      return transactionIdentifier;
    } else {
      return null;
    }
  }

  @Transactional
  @CommandHandler(logStart = CommandLogLevel.INFO, logFinish = CommandLogLevel.INFO)
  @EventEmitter(selectorName = EventConstants.SELECTOR_NAME, selectorValue = EventConstants.DELETE_ACCOUNT)
  public String deleteAccount(final DeleteAccountCommand deleteAccountCommand) {
    final String accountIdentifier = deleteAccountCommand.identifier();
    final AccountEntity accountEntity = this.accountRepository.findByIdentifier(accountIdentifier);

    final List<CommandEntity> commandEntities = this.commandRepository.findByAccount(accountEntity);
    this.commandRepository.delete((CommandEntity) commandEntities);

    this.accountRepository.delete(accountEntity);
    return accountIdentifier;
  }
}
