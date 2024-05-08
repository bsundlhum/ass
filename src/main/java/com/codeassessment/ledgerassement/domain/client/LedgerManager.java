package com.codeassessment.ledgerassement.domain.ability;

import com.codeassessment.ledgerassement.domain.model.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("unused")
@FeignClient(value = "apis", path = "/accounting/v1")
public interface LedgerManager {

    @RequestMapping(
            value = "/ledgers",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void createLedger(@RequestBody final Ledger ledger);

    @RequestMapping(
            value = "/ledgers",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    LedgerAPI fetchLedgers(@RequestParam(value = "includeSubLedgers", required = false, defaultValue = "false") final boolean includeSubLedgers,
                           @RequestParam(value = "term", required = false) final String term,
                           @RequestParam(value = "type", required = false) final String type,
                           @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                           @RequestParam(value = "size", required = false) final Integer size,
                           @RequestParam(value = "sortColumn", required = false) final String sortColumn,
                           @RequestParam(value = "sortDirection", required = false) final String sortDirection);

    @RequestMapping(
            value = "/ledgers/{identifier}",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    Ledger findLedger(@PathVariable("identifier") final String identifier);

    @RequestMapping(
            value = "/ledgers/{identifier}",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void addSubLedger(@PathVariable("identifier") final String identifier, @RequestBody final Ledger subLedger);

    @RequestMapping(
            value = "/ledgers/{identifier}",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void modifyLedger(@PathVariable("identifier") final String identifier, @RequestBody final Ledger subLedger);

    @RequestMapping(
            value = "/ledgers/{identifier}",
            method = RequestMethod.DELETE,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void deleteLedger(@PathVariable("identifier") final String identifier);

    @RequestMapping(
            value = "/ledgers/{identifier}/accounts",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    AccountAPI fetchAccountsOfLedger(@PathVariable("identifier") final String identifier,
                                     @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                                     @RequestParam(value = "size", required = false) final Integer size,
                                     @RequestParam(value = "sortColumn", required = false) final String sortColumn,
                                     @RequestParam(value = "sortDirection", required = false) final String sortDirection);

    @RequestMapping(
            value = "/accounts",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void createAccount(@RequestBody final Account account);

    @RequestMapping(
            value = "/accounts",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    AccountAPI fetchAccounts(@RequestParam(value = "includeClosed", required = false, defaultValue = "false") final boolean includeClosed,
                              @RequestParam(value = "term", required = false) final String term,
                              @RequestParam(value = "type", required = false) final String type,
                              @RequestParam(value = "includeCustomerAccounts", required = false, defaultValue = "false") final boolean includeCustomerAccounts,
                              @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                              @RequestParam(value = "size", required = false) final Integer size,
                              @RequestParam(value = "sortColumn", required = false) final String sortColumn,
                              @RequestParam(value = "sortDirection", required = false) final String sortDirection);

    @RequestMapping(
            value = "/accounts/{identifier}",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    Account findAccount(@PathVariable("identifier") final String identifier);

    @RequestMapping(
            value = "/accounts/{identifier}",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void modifyAccount(@PathVariable("identifier") final String identifier,
                       @RequestBody final Account account);


    @RequestMapping(
            value = "/accounts/{identifier}",
            method = RequestMethod.DELETE,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void deleteAccount(@PathVariable("identifier") final String identifier);

    @RequestMapping(
            value = "/accounts/{identifier}/entries",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    AccountEntryAPI fetchAccountEntries(@PathVariable("identifier") final String identifier,
                                         @RequestParam(value = "dateRange", required = false) final String dateRange,
                                         @RequestParam(value = "message", required = false) final String message,
                                         @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                                         @RequestParam(value = "size", required = false) final Integer size,
                                         @RequestParam(value = "sortColumn", required = false) final String sortColumn,
                                         @RequestParam(value = "sortDirection", required = false) final String sortDirection);

    // These helper functions are implemented here rather than in the client because it is easier to test
    // and mock if it's part of the accounting interface, rather than part of the client calling it.
    default Stream<AccountEntry> fetchAccountEntriesStream(
            final String accountIdentifier,
            final String dateRange,
            final String message,
            final String sortDirection) {
        final AccountEntryAPI firstPage = this.fetchAccountEntries(
                accountIdentifier,
                dateRange,
                message,
                0,
                10,
                null,
                null);

        final Integer pageCount = firstPage.getTotalPages();
        switch (sortDirection) {
            case "ASC":
                // Sort column is always date and order always ascending so that the order and adjacency of account
                // entries is always stable. This has the advantage that the set of account entries included in the
                // stream is set the moment the first call to fetchAccountEntries (above) is made.
                return Stream.iterate(0, (i) -> i + 1).limit(pageCount)
                        .map(i -> this.fetchAccountEntries(accountIdentifier, dateRange, message, i, 10, "transactionDate", "ASC"))
                        .flatMap(pageI -> pageI.getAccountEntries().stream());
            case "DESC":
                return Stream.iterate(pageCount - 1, (i) -> i - 1).limit(pageCount)
                        .map(i -> this.fetchAccountEntries(accountIdentifier, dateRange, message, i, 10, "transactionDate", "ASC"))
                        .flatMap(pageI -> {
                            Collections.reverse(pageI.getAccountEntries());
                            return pageI.getAccountEntries().stream();
                        });
            default:
                throw new IllegalArgumentException();
        }
    }

    @RequestMapping(
            value = "/accounts/{identifier}/commands",
            method = RequestMethod.GET,
            produces = MediaType.ALL_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    List<AccountCommand> fetchAccountCommands(@PathVariable("identifier") final String identifier);

    @RequestMapping(
            value = "/accounts/{identifier}/commands",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    void accountCommand(@PathVariable("identifier") final String identifier, @RequestBody final AccountCommand accountCommand);

    @RequestMapping(
            value = "/Postings",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    void createPostingsEntry(@RequestBody final PostingsEntry PostingsEntry);

    @RequestMapping(
            value = "/Postings",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    List<PostingsEntry> fetchPostingsEntries(@RequestParam(value = "dateRange", required = false) final String dateRange);

    @RequestMapping(
            value = "/Postings/{transactionIdentifier}",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    PostingsEntry findPostingsEntry(@PathVariable("transactionIdentifier") final String transactionIdentifier);

    @RequestMapping(
            value = "/trialbalance",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    TrialBalance getTrialBalance(
            @RequestParam(value = "includeEmptyEntries", required = false) final boolean includeEmptyEntries);

    @RequestMapping(
            value = "/chartofaccounts",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    List<ChartOfAccountEntry> getChartOfAccounts();

    @RequestMapping(
            value = "/transactiontypes",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void createTransactionType(@RequestBody @Valid final TransactionType transactionType);


    @RequestMapping(
            value = "/transactiontypes",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    TransactionTypeAPI fetchTransactionTypes(@RequestParam(value = "term", required = false) final String term,
                                              @RequestParam(value = "pageIndex", required = false) final Integer pageIndex,
                                              @RequestParam(value = "size", required = false) final Integer size,
                                              @RequestParam(value = "sortColumn", required = false) final String sortColumn,
                                              @RequestParam(value = "sortDirection", required = false) final String sortDirection);

    @RequestMapping(
            value = "/transactiontypes/{code}",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    void changeTransactionType(@PathVariable("code") final String code,
                               @RequestBody @Valid final TransactionType transactionType);

    @RequestMapping(
            value = "/transactiontypes/{code}",
            method = RequestMethod.GET,
            produces = {MediaType.ALL_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )

    TransactionType findTransactionType(@PathVariable("code") final String code);

    @RequestMapping(
            value = "/accounts/{identifier}/actions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.ALL_VALUE
    )

    List<AccountCommand> fetchActions(@PathVariable(value = "identifier") final String identifier);
}

