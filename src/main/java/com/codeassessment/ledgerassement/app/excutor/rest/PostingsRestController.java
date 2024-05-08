package com.codeassessment.ledgerassement.app.excutor.rest;


import com.codeassessment.ledgerassement.app.excutor.service.command.CreatePostingsEntryCommand;
import com.codeassessment.ledgerassement.app.excutor.service.services.AccountService;
import com.codeassessment.ledgerassement.app.excutor.service.services.CommandGateway;
import com.codeassessment.ledgerassement.app.excutor.service.services.PostingsEntryService;
import com.codeassessment.ledgerassement.app.excutor.util.DateRange;
import com.codeassessment.ledgerassement.app.excutor.util.ServiceException;
import com.codeassessment.ledgerassement.domain.PermittableGroupIds;
import com.codeassessment.ledgerassement.domain.model.AcceptedTokenType;
import com.codeassessment.ledgerassement.domain.model.Account;
import com.codeassessment.ledgerassement.domain.model.Permittable;
import com.codeassessment.ledgerassement.domain.model.PostingsEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"unused"})
@RestController
@RequestMapping("/journal")
public class PostingsRestController {

  private final CommandGateway commandGateway;
  private final PostingsEntryService journalEntryService;
  private final AccountService accountService;

  @Autowired
  public PostingsRestController(final CommandGateway commandGateway,
                               final PostingsEntryService journalEntryService,
                               final AccountService accountService) {
    super();
    this.commandGateway = commandGateway;
    this.journalEntryService = journalEntryService;
    this.accountService = accountService;
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_POSTINGS)
  @RequestMapping(
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.APPLICATION_JSON_VALUE}
  )
  @ResponseBody
  ResponseEntity<Void> createJournalEntry(@RequestBody @Valid final PostingsEntry journalEntry) {
    final Double debtorAmountSum = journalEntry.getDebtors()
        .stream()
        .peek(debtor -> {
          final Optional<Account> accountOptional = this.accountService.findAccount(debtor.getAccountNumber());
          if (!accountOptional.isPresent()) {
            throw ServiceException.badRequest("Unknown debtor account{0}.", debtor.getAccountNumber());
          }
          if (!accountOptional.get().getState().equals(Account.State.OPEN.name())) {
            throw ServiceException.conflict("Debtor account{0} must be in state open.", debtor.getAccountNumber());
          }
        })
        .map(debtor -> Double.valueOf(debtor.getAmount()))
        .reduce(0.0D, (x, y) -> x + y);

    final Double creditorAmountSum = journalEntry.getCreditors()
        .stream()
        .peek(creditor -> {
          final Optional<Account> accountOptional = this.accountService.findAccount(creditor.getAccountNumber());
          if (!accountOptional.isPresent()) {
            throw ServiceException.badRequest("Unknown creditor account{0}.", creditor.getAccountNumber());
          }
          if (!accountOptional.get().getState().equals(Account.State.OPEN.name())) {
            throw ServiceException.conflict("Creditor account{0} must be in state open.", creditor.getAccountNumber());
          }
        })
        .map(creditor -> Double.valueOf(creditor.getAmount()))
        .reduce(0.0D, (x, y) -> x + y);

    if (!debtorAmountSum.equals(creditorAmountSum)) {
      throw ServiceException.conflict(
          "Sum of debtor and sum of creditor amounts must be equals.");
    }

    this.commandGateway.process(new CreatePostingsEntryCommand(journalEntry));
    return ResponseEntity.accepted().build();
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_POSTINGS)
  @RequestMapping(
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.ALL_VALUE}
  )
  @ResponseBody
  ResponseEntity<List<PostingsEntry>> fetchJournalEntries(
      @RequestParam(value = "dateRange", required = false) final String dateRange
  ) {
    final DateRange range = DateRange.fromIsoString(dateRange);

    return ResponseEntity.ok(this.journalEntryService.fetchJournalEntries(range));
  }

  @Permittable(value = AcceptedTokenType.TENANT, groupId = PermittableGroupIds.ASSESS_POSTINGS)
  @RequestMapping(
      value = "/{transactionIdentifier}",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_VALUE},
      consumes = {MediaType.ALL_VALUE}
  )
  @ResponseBody
  ResponseEntity<PostingsEntry> findJournalEntry(
      @PathVariable("transactionIdentifier") final String transactionIdentifier
  ) {
    final Optional<PostingsEntry> optionalJournalEntry =
        this.journalEntryService.findJournalEntry(transactionIdentifier);

    if (optionalJournalEntry.isPresent()) {
      return ResponseEntity.ok(optionalJournalEntry.get());
    } else {
      throw ServiceException.notFound("Journal entry {0} not found.", transactionIdentifier);
    }
  }
}
